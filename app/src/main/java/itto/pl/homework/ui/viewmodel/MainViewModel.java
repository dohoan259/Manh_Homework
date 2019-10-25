package itto.pl.homework.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import itto.pl.homework.data.repository.DataRepository;
import itto.pl.homework.data.repository.DeviceManager;
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

    /**
     * Indicate the state of Loading process
     */
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private MutableLiveData<String> mState = new MutableLiveData<>();

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
    public LiveData<String> getState() {
        return mState;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);

        mUpdateLocationUseCase = new UpdateLocationUseCase(DataRepository.getInstance(), DeviceManager.getInstance(application));
        mUpdateBatteryUseCase = new UpdateBatteryUseCase(DataRepository.getInstance(), new DeviceManager(application));
        mSendDataUseCase = new SendDataUseCase(DataRepository.getInstance());

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
