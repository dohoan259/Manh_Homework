package itto.pl.homework.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import itto.pl.homework.R;
import itto.pl.homework.data.model.LocationItem;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class DataManager {
    private static final String TAG = "DataManager";
    private static DataManager sInstance;
    private static final Object sLock = new Object();
    private Application mContext;

    private MutableLiveData<List<String>> mDataList = new MutableLiveData<>();

    private DataManager(Application context) {
        mContext = context;
        mDataList.setValue(new ArrayList<>());
    }

    private void addDataToList(String data){
        List<String> dataList = mDataList.getValue();
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(data);
        mDataList.setValue(dataList);
    }

    public LiveData<List<String>> getDataList() {
        return mDataList;
    }

    public void clearDataList() {
        mDataList.setValue(new ArrayList<>());
    }

    public static DataManager getInstance(Application application) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null)
                    sInstance = new DataManager(application);
            }

        }
        return sInstance;
    }

    public void addBatteryInfo(int batteryLevel) {
        String battery = String.valueOf(batteryLevel);
        battery = mContext.getString(R.string.battery_info, battery);
        addDataToList(battery);
    }

    public void addLocation(LocationItem item) {
        addDataToList(item.toString());
    }
}
