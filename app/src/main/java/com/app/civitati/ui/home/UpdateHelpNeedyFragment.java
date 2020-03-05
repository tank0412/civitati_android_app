package com.app.civitati.ui.home;

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

public class UpdateHelpNeedyFragment extends Fragment implements View.OnClickListener {
    View root;
    TextView needyId;
    TextView helpInfo;
    TextView helpDate;

    public NeedyHelp needies;
    Context context;

    UpdateHelpNeedyFragment(NeedyHelp needies, Context context) {
        this.needies = needies;
        this.context = context;

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_upd_help_needy, container, false);

        needyId = (TextView)root.findViewById(R.id.needyID);
        helpInfo = (TextView)root.findViewById(R.id.helpInfo);
        helpDate = (TextView)root.findViewById(R.id.helpDate);

        Button btn = root.findViewById(R.id.needyHelpBtn);
        btn.setOnClickListener(this);

        System.out.println(needies.getId().toString());
        needyId.setText(needies.getId().toString());
        helpInfo.setText(needies.getHelpInfo());

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(needies.getHelpDate().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("dd-MM-yy");
        helpDate.setText(print.format(parsedDate));
        return root;
    }

    @Override
    public void onClick(View v) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy",
                Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(helpDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("yy-MM-dd"); // TODO: Исправить костыль с датой

        Call<ResponseBody> tryToDelete = apiInterface.updateHelpNeedy(needies.getId(), needyId.getText().toString(), helpInfo.getText().toString(), print.format(parsedDate), "UR" );
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
                    needyId.setVisibility(View.INVISIBLE);
                    helpInfo.setVisibility(View.INVISIBLE);
                    helpDate.setVisibility(View.INVISIBLE);

                    transaction.replace(R.id.helpNeedyUpd,  new HelpNeedyFragment());
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
