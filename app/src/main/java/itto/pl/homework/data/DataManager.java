package itto.pl.homework.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import itto.pl.homework.R;
import itto.pl.homework.base.IActionCallback;
import itto.pl.homework.data.device.DeviceManager;
import itto.pl.homework.data.model.LocationItem;
import itto.pl.homework.data.network.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static itto.pl.homework.base.AppConstants.DATA_MAX_SIZE;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class DataManager {
    private static final String TAG = "DataManager";
    private static DataManager sInstance;
    private static final Object sLock = new Object();

    private DeviceManager mDeviceManager;
    private RetrofitHelper mRetrofitHelper;
    private Context mContext;
    private IActionCallback mDataCallback;

    private ArrayList<String> mDataList = new ArrayList<>();


    /**
     * The state of loading process
     */
    private MutableLiveData<String> mState = new MutableLiveData<String>();

    public LiveData<String> getState() {
        return mState;
    }

    /**
     * Gps
     */
    /* To easy to check, using 6s instead of 6 minutes */
    private static final int GPS_INTERVAL = 6000;
    private Timer mGpsTimer;
    private TimerTask mGpsTimerTask = new TimerTask() {
        @Override
        public void run() {
            LocationItem item = mDeviceManager.getLastBestLocation();
            mState.postValue(mContext.getString(R.string.gps_info, item.getLatitude(), item.getLongitude()));
            mDataList.add(item.toString());
            checkForPostData();
        }
    };

    /**
     * Battery
     */
    /* To easy to check, using 9s instead of 9 minutes */
    private static final int BATTERY_INTERVAL = 9000;
    private Timer mBatteryTimer;
    private TimerTask mBatteryTimerTask = new TimerTask() {
        @Override
        public void run() {
            String battery = String.valueOf(mDeviceManager.getBatteryLevel());
            battery = mContext.getString(R.string.battery_info, battery);
            mState.postValue(battery);
            mDataList.add(battery);
            checkForPostData();
        }
    };


    public DataManager(Context context) {
        mContext = context.getApplicationContext();
        mDeviceManager = DeviceManager.getInstance(mContext);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    synchronized
    public static DataManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null)
                    sInstance = new DataManager(context);
            }

        }
        return sInstance;
    }

    /**
     * Start loading with timers
     */
    public void startLoading(IActionCallback callback) {
        mDataCallback = callback;
        // Load GPS location
        if (mGpsTimer != null) {
            mGpsTimer.cancel();
            mGpsTimer = null;
        }
        mGpsTimer = new Timer();
        mGpsTimer.schedule(mGpsTimerTask, GPS_INTERVAL, GPS_INTERVAL);

        // Load Battery
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
        }
        mBatteryTimer = new Timer();
        mBatteryTimer.schedule(mBatteryTimerTask, BATTERY_INTERVAL, BATTERY_INTERVAL);
    }

    synchronized
    private void checkForPostData() {
        if (mDataList.size() == DATA_MAX_SIZE) {
            mState.postValue(mContext.getString(R.string.send_data));
            mRetrofitHelper.sendData(mDataList, new Callback<String>() {
                @Override
                public void onResponse(Call call, @NotNull Response response) {
                    Log.w(TAG, "dataResponse: " + response.raw());
                    if (mDataCallback != null) {
                        mDataCallback.onSuccess(response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if (mDataCallback != null) {
                        mDataCallback.onSuccess(t.getMessage());
                    }
                }
            });
            mDataList.clear();
        }
    }

    /**
     * Stop collect data
     */
    public boolean stopLoading() {
        mDataCallback = null;

        if (mGpsTimer != null) {
            mGpsTimer.cancel();
            mGpsTimer = null;
        }

        // Load Battery
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
        }
        return true;
    }
}
