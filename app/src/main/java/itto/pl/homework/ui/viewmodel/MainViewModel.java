package itto.pl.homework.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import itto.pl.homework.data.DataManager;
import itto.pl.homework.data.device.DeviceManager;
import itto.pl.homework.usecase.LoadBatteryUseCase;
import itto.pl.homework.usecase.LoadLocationUseCase;
import itto.pl.homework.usecase.SendDataUseCase;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class MainViewModel extends AndroidViewModel {
    private LoadLocationUseCase mLoadLocationUseCase;
    private LoadBatteryUseCase mLoadBatteryUseCase;
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

        mLoadLocationUseCase = new LoadLocationUseCase(DataManager.getInstance(application), DeviceManager.getInstance(application));
        mLoadBatteryUseCase = new LoadBatteryUseCase(DataManager.getInstance(application), new DeviceManager(application));
        mSendDataUseCase = new SendDataUseCase(DataManager.getInstance(application));

        mIsLoading.postValue(false);
    }

    /**
     * Start loading data
     */
    public void startLoading() {
        mIsLoading.setValue(true);
        mLoadLocationUseCase.start();
        mLoadBatteryUseCase.start();
        mSendDataUseCase.start();
    }

    /**
     * Stop loading
     */
    public void stopLoading() {
        mIsLoading.setValue(false);
        mLoadLocationUseCase.stop();
        mLoadBatteryUseCase.stop();

//        mSendDataUseCase.stop();
    }
}
