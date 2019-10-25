package itto.pl.homework.data.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.util.Log;

import itto.pl.homework.data.model.LocationItem;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/

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
     * get current battery level of this device
     *
     * @return
     */
    public int getBatteryLevel() {
        int batLevel = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.d(TAG, "getBatteryLevel: " + batLevel);
        return batLevel;
    }

    /**
     * @return the last know best location
     */
    public LocationItem getLastBestLocation() {
        @SuppressLint("MissingPermission") Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        @SuppressLint("MissingPermission") Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

//        long GPSLocationTime = 0;
//        if (null != locationGPS) {
//            GPSLocationTime = locationGPS.getTime();
//        }
//
//        long NetLocationTime = 0;
//
//        if (null != locationNet) {
//            NetLocationTime = locationNet.getTime();
//        }
//
//        if (0 < GPSLocationTime - NetLocationTime) {
//            return locationGPS;
//        } else {
//            return locationNet;
//        }
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
