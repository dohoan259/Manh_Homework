package itto.pl.homework.usecase;

import java.util.Timer;
import java.util.TimerTask;


import itto.pl.homework.data.DataManager;
import itto.pl.homework.data.device.DeviceManager;

public class LoadBatteryUseCase {

    private DataManager mDataManager;
    private DeviceManager mDeviceManager;

    /**
     * Battery
     */
    /* To easy to check, using 9s instead of 9 minutes */
    private static final int BATTERY_INTERVAL = 9000;
    private Timer mBatteryTimer;
    private TimerTask mBatteryTimerTask = new TimerTask() {
        @Override
        public void run() {
            mDataManager.addBatteryInfo(mDeviceManager.getBatteryLevel());
        }
    };

    public LoadBatteryUseCase(DataManager dataManager, DeviceManager deviceManager) {
        mDataManager = dataManager;
        mDeviceManager = deviceManager;
    }

    public void start() {
        // Load Battery
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
        }
        mBatteryTimer = new Timer();
        mBatteryTimer.schedule(mBatteryTimerTask, BATTERY_INTERVAL, BATTERY_INTERVAL);
    }

    public void stop() {
        // Load Battery
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
        }
    }
}
