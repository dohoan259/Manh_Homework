package itto.pl.homework.usecase;

import java.util.Timer;
import java.util.TimerTask;


import itto.pl.homework.data.repository.DataRepository;
import itto.pl.homework.data.model.BatteryEntity;
import itto.pl.homework.data.repository.DeviceManager;

public class UpdateBatteryUseCase {

    private DataRepository mDataRepository;
    private DeviceManager mDeviceManager;

    /**
     * BatteryEntity
     */
    /* To easy to check, using 9s instead of 9 minutes */
    private static final int BATTERY_INTERVAL = 9000;
    private Timer mBatteryTimer;
    private TimerTask mBatteryTimerTask = new TimerTask() {
        @Override
        public void run() {
            mDataRepository.addBatteryInfo(new BatteryEntity(mDeviceManager.getBatteryLevel()));
        }
    };

    public UpdateBatteryUseCase(DataRepository dataRepository, DeviceManager deviceManager) {
        mDataRepository = dataRepository;
        mDeviceManager = deviceManager;
    }

    public void start() {
        // Load BatteryEntity
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
        }
        mBatteryTimer = new Timer();
        mBatteryTimer.schedule(mBatteryTimerTask, BATTERY_INTERVAL, BATTERY_INTERVAL);
    }

    public void stop() {
        // Load BatteryEntity
        if (mBatteryTimer != null) {
            mBatteryTimer.cancel();
            mBatteryTimer = null;
        }
    }
}
