package com.app.civitati.ui.dashboard;

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

public class AddNeedyFragment extends Fragment implements View.OnClickListener {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_needy, container, false);
        Button addNeedyBtn = root.findViewById(R.id.needySubmitBtn);
        addNeedyBtn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        final TextView needyName = root.findViewById(R.id.needyName);
        final TextView helpReason = root.findViewById(R.id.needyID);
        final TextView needyAddress = root.findViewById(R.id.needyAddress);
        final TextView needyTelephone = root.findViewById(R.id.needyTelephone);
        final TextView needyAddInfo = root.findViewById(R.id.needyAddInfo);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        final APIInterface apiInterfaceF = apiInterface;
        Call<ResponseBody> getNeedyCount = apiInterface.needyCount("SC" );
        getNeedyCount.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String reponse = response.body().string();
                    int needyCount = Integer.parseInt(reponse);
                    System.out.println(needyCount);

                    Call<ResponseBody> getNeedyCount = apiInterfaceF.addNeedy(needyCount+1, needyName.getText().toString(), helpReason.getText().toString(), needyAddress.getText().toString(), needyTelephone.getText().toString(), "I" );
                    getNeedyCount.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String success = "New record created successfully";
                                String reponse = response.body().string();
                                if(success.equals(reponse)) {
                                    System.out.println("Add needy success");
                                    Log.i("Civitati", "Success to add needy. ");
                                    needyAddInfo.setText(getContext().getString(R.string.report_add_success));
                                    needyAddInfo.setVisibility(View.VISIBLE);
                                }
                                else {
                                    System.out.println("Add needy fail");
                                    Log.i("Civitati", "Fail to add needy. ");
                                    needyAddInfo.setText(getContext().getString(R.string.report_add_fail));
                                    needyAddInfo.setVisibility(View.VISIBLE);

                                }
                                System.out.println(reponse);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //e.printStackTrace();
                            Log.i("Civitati", "FAIL to insert Needy. Error" );
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //e.printStackTrace();
                Log.i("Civitati", "FAIL to get count of Needy. Error" );
            }
        });
    }
}
