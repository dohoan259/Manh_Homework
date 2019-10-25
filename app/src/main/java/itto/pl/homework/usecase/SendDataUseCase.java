package itto.pl.homework.usecase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import itto.pl.homework.data.DataManager;
import itto.pl.homework.data.network.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static itto.pl.homework.base.AppConstants.DATA_MAX_SIZE;

public class SendDataUseCase extends Thread {

    private DataManager mDataManager;
    private RetrofitHelper mRetrofitHelper;

    public SendDataUseCase(DataManager dataManager) {
        mDataManager = dataManager;
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    public void run() {
        super.run();
        checkForPostData();
    }

    private void checkForPostData() {
        mDataManager.getDataList().observeForever(dataList -> {
            if (dataList.size() == DATA_MAX_SIZE) {
                // lock DataManager
                sendData(dataList);
                mDataManager.clearDataList();
                // unlock DataManager
            }
        });
    }

    private void sendData(List<String> dataList) {
        mRetrofitHelper.sendData(dataList, new Callback<String>() {
            @Override
            public void onResponse(Call call, @NotNull Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
