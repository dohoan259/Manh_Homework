package itto.pl.homework.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public interface IServerAPI {
    String FUNCTION_POST_DATA = "/list";

    @POST(FUNCTION_POST_DATA)
    Call<String> postData(@Body List<String> request);
}
