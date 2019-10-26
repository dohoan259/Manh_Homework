package itto.pl.homework.ui.thirdparty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;

import itto.pl.homework.ui.viewmodel.LocationItem;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Responsibility for get device battery/ sensor information
 */
public class DeviceManager {
    private static final String TAG = "DeviceManager";
    private volatile static DeviceManager sInstance;
    private static final Object sLock = new Object();


    private BatteryManager mBatteryManager;
    private LocationManager mLocationManager;

    public DeviceManager(Context context) {
        mBatteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        mLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static DeviceManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new DeviceManager(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * get current battery of this devices
     *
     * @return
     */
    public int getBatteryLevel() {
        return mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    /**
     * @return the last know best location
     */
    public LocationItem getLastBestLocation() {
        @SuppressLint("MissingPermission") Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        @SuppressLint("MissingPermission") Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        LocationItem location = new LocationItem();

        if (locationGPS != null) {
            location.setLatitude(locationGPS.getLatitude());
            location.setLongitude(locationGPS.getLongitude());
        } else {
            location.setLatitude(locationNet.getLatitude());
            location.setLongitude(locationNet.getLongitude());
        }
        return location;
    }
}
