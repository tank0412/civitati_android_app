package com.app.civitati.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.civitati.R;
import com.app.civitati.ui.home.RegisterFragment;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button addNeedyBtn = root.findViewById(R.id.addNeedyBtn);
        addNeedyBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.dashboard,  new AddNeedyFragment());
        Button addNeedyBtn = root.findViewById(R.id.addNeedyBtn);
        addNeedyBtn.setVisibility(View.INVISIBLE);
        transaction.commit();
    }
}