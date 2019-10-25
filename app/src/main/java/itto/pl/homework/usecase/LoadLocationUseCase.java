package itto.pl.homework.usecase;

import java.util.Timer;
import java.util.TimerTask;

import itto.pl.homework.data.DataManager;
import itto.pl.homework.data.device.DeviceManager;
import itto.pl.homework.data.model.LocationItem;

public class LoadLocationUseCase {
    private DataManager mDataManager;
    private DeviceManager mDeviceManager;

    public LoadLocationUseCase(DataManager dataManager, DeviceManager deviceManager) {
        mDataManager = dataManager;
        mDeviceManager = deviceManager;
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
            mDataManager.addLocation(item);
        }
    };

    public void start() {
        // Load GPS location
        if (mGpsTimer != null) {
            mGpsTimer.cancel();
            mGpsTimer = null;
        }
        mGpsTimer = new Timer();
        mGpsTimer.schedule(mGpsTimerTask, GPS_INTERVAL, GPS_INTERVAL);
    }

    public void stop() {
        if (mGpsTimer != null) {
            mGpsTimer.cancel();
            mGpsTimer = null;
        }
    }
}
