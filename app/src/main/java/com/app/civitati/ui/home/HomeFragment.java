package com.app.civitati.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        Button button = (Button)root.findViewById(R.id.loginBtn);
        button.setOnClickListener(this);
        return root;
    }

    public void onClick(View v) {
        final TextView enterPassword = root.findViewById(R.id.enterPassword);
        final TextView enterLogin = root.findViewById(R.id.enterLogin);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> tryToRegister = apiInterface.registerInApp(enterLogin.getText().toString(), enterPassword.getText().toString(), "I" );

        tryToRegister.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body().string());
                    Log.i("Civitati", "Success in register. ");
                    Log.i("Civitati",  response.body().string() );
                    enterPassword.setVisibility(View.GONE);
                    enterLogin.setVisibility(View.GONE);
                    Button button = (Button)root.findViewById(R.id.loginBtn);
                    button.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //e.printStackTrace();
                Log.i("Civitati", "FAIL to register. Error is" );
            }
        });

    }
}