package com.app.civitati;

import com.app.civitati.ui.dashboard.Needy;
import com.app.civitati.ui.home.NeedyHelp;
import com.app.civitati.ui.home.ServerResponse;
import com.app.civitati.ui.notifications.Notification;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/users.php")
    Call<ResponseBody> registerInApp(@Query("NICKNAME") String nickName, @Query("PASSWORD") String password, @Query("TYPE") String type );

    @GET("/users.php")
    Call<ResponseBody> loginInApp(@Query("NICKNAME") String nickName, @Query("PASSWORD") String password, @Query("TYPE") String type );

    @GET("/needy.php")
    Call<ResponseBody> addNeedy(@Query("ID") int id, @Query("NAME") String name, @Query("HELP_REASON") String reason, @Query("ADDRESS") String address, @Query("TELEPHONE") String telephone, @Query("TYPE") String type );

    @GET("/needy.php")
    Call<ResponseBody> needyCount(@Query("TYPE") String type );

    @GET("/needy.php")
    Call<ArrayList<Needy>>getAllNeedies(@Query("TYPE") String type );

    @GET("/helpneedy.php")
    Call<ResponseBody> addHelpNeedy(@Query("ID") int id, @Query("ASSISTANT_NICKNAME") String assistantNickname, @Query("NEEDY_ID") String needyId, @Query("HELP_INFO") String helpInfo, @Query("HELP_DATE") String helpDate, @Query("TYPE") String type );

    @GET("/helpneedy.php")
    Call<ResponseBody> helpNeedyCount(@Query("TYPE") String type );

    @GET("/helpneedy.php")
    Call<ArrayList<NeedyHelp>>getAllHelpNeediesInfoOfCurrentUser(@Query("ASSISTANT_NICKNAME") String assistantNickname, @Query("TYPE") String type );

    @GET("/helpneedy.php")
    Call<ResponseBody> deleteHNRow(@Query("ID") int id, @Query("ASSISTANT_NICKNAME") String assistantNickname, @Query("TYPE") String type );

    @GET("/needy.php")
    Call<ResponseBody> deleteNRow(@Query("ID") int id,  @Query("TYPE") String type );

    @GET("/helpneedy.php")
    Call<ResponseBody> updateHelpNeedy(@Query("ID") int id, @Query("NEEDY_ID") String needyId, @Query("HELP_INFO") String helpInfo, @Query("HELP_DATE") String helpDate, @Query("TYPE") String type );

    @GET("/needy.php")
    Call<ResponseBody> updNeedy(@Query("ID") int id, @Query("NAME") String name, @Query("HELP_REASON") String reason, @Query("ADDRESS") String address, @Query("TELEPHONE") String telephone, @Query("TYPE") String type );

    @GET("/notifications.php")
    Call<ArrayList<Notification>> getNotificationsForCurrentUser(@Query("USER_NICKNAME") String USER_NICKNAME, @Query("TYPE") String type );

    @GET("/notifications.php")
    Call<ResponseBody> deleteNotifRow(@Query("ID") int id, @Query("USER_NICKNAME") String USER_NICKNAME,  @Query("TYPE") String type );

    @Multipart
    @POST("upload.php")
    Call<ResponseBody> updateProfile(@Part MultipartBody.Part image);

    @POST("upload.php")
    Call<ResponseBody> profileImage(@Body RequestBody body);

    @Multipart
    @POST("upload.php")
    Call<ServerResponse> upload(
            @PartMap Map<String, RequestBody> map
    );

}