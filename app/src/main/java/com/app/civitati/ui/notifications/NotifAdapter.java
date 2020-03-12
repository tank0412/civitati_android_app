package com.app.civitati.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.civitati.APIClient;
import com.app.civitati.APIInterface;
import com.app.civitati.R;
import com.app.civitati.ui.dashboard.Needy;
import com.app.civitati.ui.dashboard.UpdateNeedyFragment;

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

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotificationViewHolder>{
    List<Notification> notifications;
    Context context;
    FragmentManager fragmentManager;
    View helpNeedyView;
    NotifAdapter(List<Notification> notifications, Context context, FragmentManager fragmentManager, View helpNeedyView){
        this.notifications = notifications;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.helpNeedyView = helpNeedyView;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_row, viewGroup, false);
        NotificationViewHolder nvh = new NotificationViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.notificationId.setText("ID: " + notifications.get(position).getId().toString());
        holder.NEEDY_HELP_ID.setText("Needy Help ID: " + notifications.get(position).getNEEDY_HELP_ID());

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(notifications.get(position).getNOTIFICATION_DATE().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("dd-MM-yy");
        //System.out.println(print.format(parsedDate));
        //System.out.println(parsedDate.toString());
        holder.NOTIFICATION_DATE.setText("Notification date: " + print.format(parsedDate));

        final NotificationViewHolder holderF = holder;
        final int positionF = position;
        holder.iv_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, holderF.iv_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_short, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

                        String userNickName = null;
                        SharedPreferences mySharedPreferences = context.getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
                        if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
                            userNickName = mySharedPreferences.getString("CIVITATI_PREFERENCES", "");
                        }

                        Call<ResponseBody> tryToDelete = apiInterface.deleteNotifRow(notifications.get(positionF).getId() , userNickName, "DR" );
                        tryToDelete.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String success = "Record was deleted successfully";
                                String reponseString = null;
                                try {
                                    reponseString = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(reponseString);
                                Log.i("Civitati",  reponseString );
                                if(success.equals(reponseString)) {
                                    Log.i("Civitati", "Success to delete row. ");
                                    Toast.makeText(context,"Record was successfully deleted", Toast.LENGTH_SHORT).show();
                                    notifications.remove(positionF);
                                    notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(context,"Error: Record was not deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.i("Civitati", "Error: Record was not deleted" );
                            }
                        });
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView notificationId;
        TextView NEEDY_HELP_ID;
        TextView NOTIFICATION_DATE;
        ImageView iv_menu;
        NotificationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.needyList);
            notificationId = (TextView)itemView.findViewById(R.id.id);
            NEEDY_HELP_ID = (TextView)itemView.findViewById(R.id.needyHelpId);
            NOTIFICATION_DATE = (TextView)itemView.findViewById(R.id.notificationDate);
            iv_menu = itemView.findViewById(R.id.iv_menu);
        }
    }
}