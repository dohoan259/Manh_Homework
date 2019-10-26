package itto.pl.homework.usecase;

import androidx.lifecycle.Observer;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import itto.pl.homework.data.repository.DataRepository;
import itto.pl.homework.data.network.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static itto.pl.homework.base.AppConstants.DATA_MAX_SIZE;

public class SendDataUseCase {

    private ExecutorService executorService;
    private DataRepository mDataRepository;
    private RetrofitHelper mRetrofitHelper;
    private Future future;
    private Observer<List<String>> mObserver;

    public SendDataUseCase(DataRepository dataRepository) {
        mDataRepository = dataRepository;
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
        mObserver = dataList -> {
            if (dataList.size() == DATA_MAX_SIZE) {
                // lock DataRepository
                synchronized (DataRepository.class) {
                    sendData(dataList);
                    mDataRepository.clearDataList();
                }
                // unlock DataRepository
            }
        };
        mDataRepository.getDataList().observeForever(mObserver);
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
        mDataRepository.getDataList().removeObserver(mObserver);
        future.cancel(true);
        executorService.shutdownNow();
    }
}
