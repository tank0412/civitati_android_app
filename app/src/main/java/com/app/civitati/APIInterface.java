package com.app.civitati;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/crud.php")
    Call<ResponseBody> registerInApp(@Query("NICKNAME") String nickName, @Query("PASSWORD") String password, @Query("TYPE") String type );

}