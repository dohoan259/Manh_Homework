package itto.pl.homework.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import itto.pl.homework.data.model.BatteryEntity;
import itto.pl.homework.data.model.LocationEntity;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class DataRepository {
    private static final String TAG = "DataRepository";
    private static DataRepository sInstance;
    private static final Object sLock = new Object();

    private MutableLiveData<List<String>> mDataList = new MutableLiveData<>();

    private MutableLiveData<BatteryEntity> mLatestBattery = new MutableLiveData<>();
    private MutableLiveData<LocationEntity> mLatestLocation = new MutableLiveData<>();

    private DataRepository() {
        mDataList.setValue(new ArrayList<>());
    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null)
                    sInstance = new DataRepository();
            }

        }
        return sInstance;
    }

    private void addDataToList(String data){
        List<String> dataList = mDataList.getValue();
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(data);
        mDataList.postValue(dataList);
    }

    public LiveData<List<String>> getDataList() {
        return mDataList;
    }

    public void clearDataList() {
        mDataList.setValue(new ArrayList<>());
    }

    public void addBatteryInfo(BatteryEntity batteryEntity) {
        addDataToList(batteryEntity.toString());
        mLatestBattery.postValue(batteryEntity);
    }

    public void addLocation(LocationEntity locationEntity) {
        addDataToList(locationEntity.toString());
        mLatestLocation.postValue(locationEntity);
    }

    public MutableLiveData<BatteryEntity> getLatestBattery() {
        return mLatestBattery;
    }

    public MutableLiveData<LocationEntity> getLatestLocation() {
        return mLatestLocation;
    }
}
