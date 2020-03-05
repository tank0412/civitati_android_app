package com.app.civitati.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNeedyFragment extends Fragment implements View.OnClickListener {
    View root;
    TextView needyName;
    TextView helpReason;
    TextView needyAddress;
    TextView needyTelephone;

    public Needy needies;
    Context context;

    UpdateNeedyFragment(Needy needies, Context context) {
        this.needies = needies;
        this.context = context;

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_upd_needy, container, false);

        needyName = (TextView)root.findViewById(R.id.needyName);
        helpReason = (TextView)root.findViewById(R.id.helpReason);
        needyAddress = (TextView)root.findViewById(R.id.needyAddress);
        needyTelephone = (TextView)root.findViewById(R.id.needyTelephone);

        needyName.setText(needies.getName());
        helpReason.setText(needies.getHelpReason());
        needyAddress.setText(needies.getAdress());
        needyTelephone.setText(needies.getTelephone().toString());

        Button btn = root.findViewById(R.id.needyHelpBtn);
        btn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        Call<ResponseBody> tryToDelete = apiInterface.updNeedy(needies.getId(), needyName.getText().toString(), helpReason.getText().toString(), needyAddress.getText().toString(),needyTelephone.getText().toString(), "UR" );
        tryToDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String success = "Record was updated successfully";
                String reponseString = null;
                try {
                    reponseString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(reponseString);
                Log.i("Civitati",  reponseString );
                if(success.equals(reponseString)) {
                    Log.i("Civitati", "Success to update row. ");
                    Toast.makeText(context,"Record was successfully updated", Toast.LENGTH_SHORT).show();

                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                    Button btn = root.findViewById(R.id.needyHelpBtn);
                    btn.setVisibility(View.INVISIBLE);
                    needyName.setVisibility(View.INVISIBLE);
                    helpReason.setVisibility(View.INVISIBLE);
                    needyAddress.setVisibility(View.INVISIBLE);
                    needyTelephone.setVisibility(View.INVISIBLE);

                    transaction.replace(R.id.NeedyUpd,  new DashboardFragment());
                    transaction.commit();
                }
                else {
                    Toast.makeText(context,"Error: Record was not updated", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Civitati", "Error: Record was not updated" );
            }
        });

    }
}