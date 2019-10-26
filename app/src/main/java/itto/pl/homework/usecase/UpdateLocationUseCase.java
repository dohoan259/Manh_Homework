package itto.pl.homework.usecase;

import java.util.Timer;
import java.util.TimerTask;

import itto.pl.homework.data.repository.DataRepository;
import itto.pl.homework.data.model.LocationEntity;
import itto.pl.homework.ui.thirdparty.DeviceManager;
import itto.pl.homework.ui.viewmodel.LocationItem;

public class UpdateLocationUseCase {
    private DataRepository mDataRepository;
    private DeviceManager mDeviceManager;

    public UpdateLocationUseCase(DataRepository dataRepository, DeviceManager deviceManager) {
        mDataRepository = dataRepository;
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
            mDataRepository.addLocation(new LocationEntity(item.getLatitude(), item.getLongitude()));
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
