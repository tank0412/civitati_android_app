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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;
import com.app.civitati.ui.dashboard.AddNeedyFragment;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpNeedyFragment  extends Fragment implements View.OnClickListener{
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_help_needy, container, false);
        Button button = (Button)root.findViewById(R.id.addHelpNeedyBtn);
        button.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.helpNeedyInfo,  new AddHelpNeedyFragment());
        Button addNeedyBtn = root.findViewById(R.id.addHelpNeedyBtn);
        addNeedyBtn.setVisibility(View.INVISIBLE);
        transaction.commit();
    }
}
