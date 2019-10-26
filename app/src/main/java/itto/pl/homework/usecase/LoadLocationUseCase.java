package itto.pl.homework.usecase;

import androidx.lifecycle.LiveData;

import itto.pl.homework.data.model.LocationEntity;
import itto.pl.homework.data.repository.DataRepository;

public class LoadLocationUseCase {

    private DataRepository mDataRepository;

    public LoadLocationUseCase(DataRepository mDataRepository) {
        this.mDataRepository = mDataRepository;
    }

    public LiveData<LocationEntity> loadLatestLocation() {
        return mDataRepository.getLatestLocation();
    }
}
