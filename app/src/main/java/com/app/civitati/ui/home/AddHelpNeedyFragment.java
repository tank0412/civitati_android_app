package com.app.civitati.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHelpNeedyFragment  extends Fragment implements View.OnClickListener {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_help_needy, container, false);
        Button button = (Button)root.findViewById(R.id.needySubmitBtn);
        button.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View v) {
        final TextView needyID = root.findViewById(R.id.needyID);
        final TextView helpInfo = root.findViewById(R.id.helpInfo);
        final TextView helpDate = root.findViewById(R.id.helpDate);
        final TextView needyHelpAddInfo = root.findViewById(R.id.needyHelpAddInfo);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> helpNeedyCount = apiInterface.helpNeedyCount("SC" );
        final APIInterface apiInterfaceF = apiInterface;
        helpNeedyCount.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String reponse = null;
                try {
                    reponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int needyHelpCount = Integer.parseInt(reponse);
                System.out.println(needyHelpCount);


                String userNickName = null;
                SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
                if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
                    userNickName = mySharedPreferences.getString("CIVITATI_PREFERENCES", "");
                }
                Call<ResponseBody> addHelpNeedy = apiInterfaceF.addHelpNeedy(needyHelpCount+1,userNickName, needyID.getText().toString(), helpInfo.getText().toString(), helpDate.getText().toString(), "I"  );
                addHelpNeedy.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String success = "New record created successfully";
                        String reponse = null;
                        try {
                            reponse = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(success.equals(reponse)) {
                            System.out.println("Add needy add success");
                            Log.i("Civitati", "Success to add needy add. ");
                            needyHelpAddInfo.setText("Needy help add success");
                            needyHelpAddInfo.setVisibility(View.VISIBLE);
                        }
                        else {
                            System.out.println("Add needy add fail");
                            Log.i("Civitati", "Fail to add needy add. ");
                            needyHelpAddInfo.setText("Needy help add fail");
                            needyHelpAddInfo.setVisibility(View.VISIBLE);

                        }
                        System.out.println(reponse);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //e.printStackTrace();
                Log.i("Civitati", "FAIL to get count of Needy. Error" );
            }
        });
    }
}
