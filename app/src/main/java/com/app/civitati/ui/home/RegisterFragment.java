package com.app.civitati.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private View root;
    private View rootHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_register, container, false);
        rootHome = inflater.inflate(R.layout.fragment_home, container, false);
        Button buttonReg = (Button)root.findViewById(R.id.registerBtn);
        buttonReg.setOnClickListener(this);
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
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.regFrag,  new HomeFragment());
                    transaction.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //e.printStackTrace();
                enterPassword.setVisibility(View.INVISIBLE);
                enterLogin.setVisibility(View.INVISIBLE);
                Button button = (Button)root.findViewById(R.id.registerBtn);
                button.setVisibility(View.INVISIBLE);
                Log.i("Civitati", "FAIL to register. Error is" );
            }
        });

    }
}
