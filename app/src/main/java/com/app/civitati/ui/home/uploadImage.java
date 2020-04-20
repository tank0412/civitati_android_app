package com.app.civitati.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class uploadImage extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String picturePath;

    public uploadImage(Context context, String picturePath  ) {
        this.context = context;
        this.picturePath = picturePath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        File file = new File(picturePath);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file);
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody2);
        Call<ServerResponse> upload = apiInterface.upload(map);
        upload.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(context, serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(context, serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.e("Retro", serverResponse.getMessage());
                } else {
                    Toast.makeText(context, serverResponse.toString(),Toast.LENGTH_SHORT).show();
                    Log.v("Response", serverResponse.toString());
                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.v("Response", "Fail" + t.getMessage());
                Toast.makeText(context, "Fail" + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
