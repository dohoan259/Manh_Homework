package itto.pl.homework.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import itto.pl.homework.R;
import itto.pl.homework.data.repository.DataRepository;
import itto.pl.homework.ui.thirdparty.DeviceManager;
import itto.pl.homework.usecase.LoadBatteryUseCase;
import itto.pl.homework.usecase.LoadLocationUseCase;
import itto.pl.homework.usecase.UpdateBatteryUseCase;
import itto.pl.homework.usecase.UpdateLocationUseCase;
import itto.pl.homework.usecase.SendDataUseCase;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class MainViewModel extends AndroidViewModel {
    private UpdateLocationUseCase mUpdateLocationUseCase;
    private UpdateBatteryUseCase mUpdateBatteryUseCase;
    private SendDataUseCase mSendDataUseCase;
    private LoadLocationUseCase mLoadLocationUseCase;
    private LoadBatteryUseCase mLoadBatteryUseCase;

    /**
     * Indicate the state of Loading process
     */
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

    public LiveData<Boolean> getLoadingState() {
        return mIsLoading;
    }

    /**
     * The response from Server
     */
    private MutableLiveData<String> mResult = new MutableLiveData<>();

    public LiveData<String> getResult() {
        return mResult;
    }

    /**
     * Get the current status
     * @return
     */
    public LiveData<String> getLocationState() {
        MediatorLiveData<String> state = new MediatorLiveData<>();
        state.addSource(mLoadLocationUseCase.loadLatestLocation(), locationEntity ->
            state.setValue(String.format(getApplication().getString(R.string.gps_info), locationEntity.getLatitude(), locationEntity.getLongitude()))
        );

        return state;
    }

    public LiveData<String> getBatteryState() {
        MediatorLiveData<String> state = new MediatorLiveData<>();
        state.addSource(mLoadBatteryUseCase.loadLatestBattery(), batteryEntity ->
                state.setValue(String.format(getApplication().getString(R.string.battery_info), batteryEntity.getLevel()))
        );

        return state;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);

        mUpdateLocationUseCase = new UpdateLocationUseCase(DataRepository.getInstance(), DeviceManager.getInstance(application));
        mUpdateBatteryUseCase = new UpdateBatteryUseCase(DataRepository.getInstance(), new DeviceManager(application));
        mSendDataUseCase = new SendDataUseCase(DataRepository.getInstance());
        mLoadLocationUseCase = new LoadLocationUseCase(DataRepository.getInstance());
        mLoadBatteryUseCase = new LoadBatteryUseCase(DataRepository.getInstance());

        mIsLoading.postValue(false);
    }

    /**
     * Start loading data
     */
    public void startLoading() {
        mIsLoading.setValue(true);
        mUpdateLocationUseCase.start();
        mUpdateBatteryUseCase.start();
        mSendDataUseCase.start();
    }

    /**
     * Stop loading
     */
    public void stopLoading() {
        mIsLoading.setValue(false);
        mUpdateLocationUseCase.stop();
        mUpdateBatteryUseCase.stop();
        mSendDataUseCase.stop();
    }
}
