package itto.pl.homework.data.network;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public class RetrofitHelper {
    private static final String TAG = "RetrofitHelper";
    private Retrofit mRetrofit;
    private static IServerAPI mServerAPI;
    private static final String BASE_URL = "http://sigma-solutions.eu";
    private static RetrofitHelper sInstance;

    public static RetrofitHelper getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitHelper();
        }
        return sInstance;
    }

    private RetrofitHelper() {
        mServerAPI = getRetrofitInstance().create(IServerAPI.class);
    }

    /**
     * Provide OkHttpClient, will used for logging request and response
     *
     * @return
     */
    private OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(s -> Log.d(TAG, s));
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        return client;
    }

    private Retrofit getRetrofitInstance() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideHttpClient())
                    .build();
        }
        return mRetrofit;
    }

    /**
     * Send data collected to server
     *
     * @param data
     * @param callback
     */
    public void sendData(List<String> data, Callback callback) {
        mServerAPI.postData(data).enqueue(callback);
    }
}
