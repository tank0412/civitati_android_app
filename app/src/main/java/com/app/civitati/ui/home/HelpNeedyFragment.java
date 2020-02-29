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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;
import com.app.civitati.ui.dashboard.AddNeedyFragment;
import com.app.civitati.ui.dashboard.NAdapter;
import com.app.civitati.ui.dashboard.Needy;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpNeedyFragment  extends Fragment implements View.OnClickListener{
    View root;
    ArrayList<NeedyHelp> needyHelpArrayList;
    NHAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_help_needy, container, false);
        Button button = (Button)root.findViewById(R.id.addHelpNeedyBtn);
        button.setOnClickListener(this);

        RecyclerView rv = root.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        needyHelpArrayList = new ArrayList<NeedyHelp>();

        adapter = new NHAdapter(needyHelpArrayList );
        rv.setAdapter(adapter);
        getAllNeediesHelp();

        return root;
    }

    @Override
    public void onClick(View v) {
        RecyclerView rv = root.findViewById(R.id.rv);
        rv.setVisibility(View.INVISIBLE);
        System.out.println("CLICK");
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.helpNeedyInfo,  new AddHelpNeedyFragment());
        Button addNeedyBtn = root.findViewById(R.id.addHelpNeedyBtn);
        addNeedyBtn.setVisibility(View.INVISIBLE);
        transaction.commit();
    }

    private void getAllNeediesHelp() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        String userNickName = null;
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
        if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
            userNickName = mySharedPreferences.getString("CIVITATI_PREFERENCES", "");
        }

        Call<ArrayList<NeedyHelp>> tryToRegister = apiInterface.getAllHelpNeediesInfoOfCurrentUser( userNickName,"SU" );

        tryToRegister.enqueue(new Callback<ArrayList<NeedyHelp>>() {
            @Override
            public void onResponse(Call<ArrayList<NeedyHelp>> call, Response<ArrayList<NeedyHelp>> response) {
                ArrayList<NeedyHelp> jsonArray = response.body();
                needyHelpArrayList.addAll(jsonArray);
                Log.i("Civitati", "GOT NEEDY HELP ARRAY" );
                System.out.println("GOT NEEDDY HELP");
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ArrayList<NeedyHelp>> call, Throwable t) {
                Log.i("Civitati", t.getMessage() );
                Log.i("Civitati", "FAIL to get all needies help." );
            }
        });
    }
}
