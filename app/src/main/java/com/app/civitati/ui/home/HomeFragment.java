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
        makeElementVisible();
        Button button = (Button)root.findViewById(R.id.loginBtn);
        button.setOnClickListener(this);

        Button buttonReg = (Button)root.findViewById(R.id.enterRegBtn);


        final TextView enterPassword = root.findViewById(R.id.enterPassword);
        final TextView enterLogin = root.findViewById(R.id.enterLogin);
        final TextView logInfo = root.findViewById(R.id.loginInfo);
        final Button buttonLogin = button;
        final Button buttonRegFinal = (Button)root.findViewById(R.id.enterRegBtn);

        View.OnClickListener oclBtnRegister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.regHome,  new RegisterFragment());
                //transaction.addToBackStack(null); // TODO: Исправить баг: при нажатии на кнопку Назад, вернет к пред фрагменту, где будет скрыт layout

                enterPassword.setVisibility(View.INVISIBLE);
                enterLogin.setVisibility(View.INVISIBLE);
                logInfo.setVisibility(View.INVISIBLE);
                buttonRegFinal.setVisibility(View.INVISIBLE);
                buttonLogin.setVisibility(View.INVISIBLE);


                transaction.commit();
            }
        };
        buttonReg.setOnClickListener(oclBtnRegister);
        return root;
    }

    public void makeElementVisible() {
        final TextView enterPassword = root.findViewById(R.id.enterPassword);
        final TextView enterLogin = root.findViewById(R.id.enterLogin);
        final Button buttonLogin = root.findViewById(R.id.loginBtn);
        final Button buttonRegFinal = (Button)root.findViewById(R.id.enterRegBtn);
        final TextView logInfo = root.findViewById(R.id.loginInfo);
        enterPassword.setVisibility(View.VISIBLE);
        enterLogin.setVisibility(View.VISIBLE);
        buttonRegFinal.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.VISIBLE);
        logInfo.setVisibility(View.INVISIBLE);
        Log.i("Civitati", "Call makeElementVisible... ");
    }

    public void onClick(View v) {
        final TextView enterPassword = root.findViewById(R.id.enterPassword);
        final TextView enterLogin = root.findViewById(R.id.enterLogin);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> tryToRegister = apiInterface.loginInApp(enterLogin.getText().toString(), enterPassword.getText().toString(), "S" );

        tryToRegister.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String success = "Login success";
                    String fail = "Login fail";
                    String reposne = response.body().string();
                    TextView loginInfo = root.findViewById(R.id.loginInfo);
                    if(success.equals(reposne)) {
                        System.out.println("Login success");
                        Log.i("Civitati", "Success to login. ");
                        loginInfo.setText("Login success");
                        loginInfo.setVisibility(View.VISIBLE);
                        //Log.i("Civitati", response.body().string());
                    }
                    if(fail.equals(reposne)) {
                        System.out.println("Login fail");
                        Log.i("Civitati", "Fail to login. ");
                        loginInfo.setText("Login fail");
                        loginInfo.setVisibility(View.VISIBLE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //e.printStackTrace();
                Log.i("Civitati", "FAIL to login. Error" );
            }
        });

    }
}