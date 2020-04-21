package com.app.civitati.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;
import com.app.civitati.ui.home.HomeFragment;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    View root;
    ArrayList<Needy> needyArrayList;
    NAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button addNeedyBtn = root.findViewById(R.id.addNeedyBtn);
        addNeedyBtn.setOnClickListener(this);

        RecyclerView rv = root.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        needyArrayList = new ArrayList<Needy>();
        /*
        Needy needy = new Needy();
        needy.setAdress("a");
        needy.setHelpReason("b");
        needy.setId(1);
        needy.setName("b");
        needy.setTelephone(BigInteger.valueOf(345645345));
        needyArrayList.add(needy);
         */

        adapter = new NAdapter(needyArrayList, getContext(), getChildFragmentManager(), root );
        rv.setAdapter(adapter);
        getAllNeedies();
        return root;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.dashboard, new AddNeedyFragment());
        Button addNeedyBtn = root.findViewById(R.id.addNeedyBtn);
        addNeedyBtn.setVisibility(View.INVISIBLE);
        RecyclerView rv = root.findViewById(R.id.rv);
        rv.setVisibility(View.INVISIBLE);
        transaction.commit();
    }

    public void getAllNeedies() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<Needy>> tryToRegister = apiInterface.getAllNeedies( "SA" );

        tryToRegister.enqueue(new Callback<ArrayList<Needy>>() {
            @Override
            public void onResponse(Call<ArrayList<Needy>> call, Response<ArrayList<Needy>> response) {
                ArrayList<Needy> jsonArray = response.body();
                if(jsonArray != null) {
                    needyArrayList.addAll(jsonArray);
                }
                else {
                    return;
                }
                Log.i("Civitati", "GOT NEEDY ARRAY" );
                System.out.println("GOT NEEDDY");
                adapter.notifyDataSetChanged();

                /*
                for (int i = 0; i < jsonArray.size(); i++) {
                    Needy needy = (Needy) jsonArray.get(i);

                    int id = needy.getId();
                    //System.out.println(String.valueOf(id));
                    String name = needy.getName();
                    //System.out.println(name);
                    String helpReason =needy.getHelpReason();
                    //System.out.println(helpReason);
                    String address = needy.getAdress();
                    //System.out.println(address);
                    BigInteger telephone = needy.getTelephone();
                    //System.out.println(String.valueOf(telephone));
                    Date submitDate = needy.getSubmitDate();
                    //System.out.println(submitDate);
                }
                 */

            }
            @Override
            public void onFailure(Call<ArrayList<Needy>> call, Throwable t) {
                Log.i("Civitati", t.getMessage() );
                Log.i("Civitati", "FAIL to get all needies." );
            }
        });
    }
}