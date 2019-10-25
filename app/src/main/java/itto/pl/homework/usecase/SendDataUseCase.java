package itto.pl.homework.usecase;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import itto.pl.homework.data.DataManager;
import itto.pl.homework.data.network.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static itto.pl.homework.base.AppConstants.DATA_MAX_SIZE;

public class SendDataUseCase {

    private ExecutorService executorService;
    private DataManager mDataManager;
    private RetrofitHelper mRetrofitHelper;
    private Future future;

    public SendDataUseCase(DataManager dataManager) {
        mDataManager = dataManager;
        mRetrofitHelper = RetrofitHelper.getInstance();

        executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        future = executorService.submit(() -> {
            checkForPostData();
            return 1;
        });
    }

    private void checkForPostData() {
        mDataManager.getDataList().observeForever(dataList -> {
            if (dataList.size() == DATA_MAX_SIZE) {
                // lock DataManager
                synchronized (DataManager.class) {
                    sendData(dataList);
                    mDataManager.clearDataList();
                }
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

    public void stop() {
        future.cancel(true);
        executorService.shutdownNow();
    }
}
