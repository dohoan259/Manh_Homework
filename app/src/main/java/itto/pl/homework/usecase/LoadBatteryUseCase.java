package itto.pl.homework.usecase;

import androidx.lifecycle.LiveData;

import itto.pl.homework.data.model.BatteryEntity;
import itto.pl.homework.data.repository.DataRepository;

public class LoadBatteryUseCase {

    private DataRepository mDataRepository;

    public LoadBatteryUseCase(DataRepository mDataRepository) {
        this.mDataRepository = mDataRepository;
    }

    public LiveData<BatteryEntity> loadLatestBattery() {
        return mDataRepository.getLatestBattery();
    }
}
