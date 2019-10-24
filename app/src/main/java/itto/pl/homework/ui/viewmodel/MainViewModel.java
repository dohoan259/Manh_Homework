package itto.pl.homework.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import itto.pl.homework.base.IActionCallback;
import itto.pl.homework.data.DataManager;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class MainViewModel extends AndroidViewModel {
    private DataManager mDataManager;

    /**
     * Indicate the state of Loading process
     */
    MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

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
        return mDataManager.getState();
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        mDataManager = DataManager.getInstance(application);
        mIsLoading.postValue(false);
    }

    /**
     * Start loading data
     */
    public void startLoading() {
        mIsLoading.postValue(true);
        mDataManager.startLoading(new IActionCallback() {
            @Override
            public void onSuccess(Object result) {
                mResult.postValue(result != null ? result.toString() : "null");
            }

            @Override
            public void onFailed(@Nullable Object error) {
                mResult.postValue(error != null ? error.toString() : "error null");
            }
        });
    }

    /**
     * Stop loading
     */
    public void stopLoading() {
        mIsLoading.postValue(!mDataManager.stopLoading());
    }
}
