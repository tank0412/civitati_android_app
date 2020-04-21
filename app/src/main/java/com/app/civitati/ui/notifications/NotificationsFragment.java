package com.app.civitati.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    ArrayList<Notification> notificationArrayList;
    NotifAdapter adapter;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView rv = root.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        notificationArrayList = new ArrayList<Notification>();
        adapter = new NotifAdapter(notificationArrayList, getContext(), getChildFragmentManager(), root );
        rv.setAdapter(adapter);
        rv.setVisibility(View.VISIBLE);
        getAllUserNotifications();
        return root;
    }

    public void getAllUserNotifications() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        String userNickName = null;
        SharedPreferences mySharedPreferences = getContext().getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
        if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
            userNickName = mySharedPreferences.getString("CIVITATI_PREFERENCES", "");
        }
        else {
            TextView report_no_internet = root.findViewById(R.id.report_notifications);
            report_no_internet.setText(getContext().getString(R.string.report_login_needed_in_notifications));
            report_no_internet.setVisibility(View.VISIBLE);
            return;
        }
        final String userNickNameF = userNickName;

        Call<ArrayList<Notification>> tryToRegister = apiInterface.getNotificationsForCurrentUser(userNickNameF, "SU" );

        tryToRegister.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                ArrayList<Notification> jsonArray = response.body();
                if(jsonArray != null) {
                    if(jsonArray.size() > 0) {
                        notificationArrayList.addAll(jsonArray);
                    }
                    else {
                        RecyclerView rv = root.findViewById(R.id.rv);
                        rv.setVisibility(View.GONE);
                        TextView report_in_help_needy = root.findViewById(R.id.report_notifications);
                        report_in_help_needy.setText(R.string.report_no_notifications);
                        report_in_help_needy.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                else {
                    RecyclerView rv = root.findViewById(R.id.rv);
                    rv.setVisibility(View.GONE);
                    TextView noInternetReport = root.findViewById(R.id.report_notifications); //in notifications textview we ask to login if it is guest
                    noInternetReport.setVisibility(View.VISIBLE);
                    return;
                }
                Log.i("Civitati", "GOT NOTIFICATION ARRAY" );
                System.out.println("GOT NOTIFICATION");
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                Log.i("Civitati", t.getMessage() );
                Log.i("Civitati", "FAIL to get all notifications." );
            }
        });
    }
}