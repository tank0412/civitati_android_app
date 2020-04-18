package com.app.civitati.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class NHAdapter extends RecyclerView.Adapter<NHAdapter.NeedyViewHolder>{
    List<NeedyHelp> needies;
    Context context;
    FragmentManager fragmentManager;
    View helpNeedyView;
    NHAdapter(List<NeedyHelp> needies, Context context, FragmentManager fragmentManager, View helpNeedyView){
        this.needies = needies;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.helpNeedyView = helpNeedyView;
    }

    @NonNull
    @Override
    public NeedyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.needy_help_list_row, viewGroup, false);
        NeedyViewHolder nvh = new NeedyViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NeedyViewHolder holder, final int position) {
        holder.needyHelpId.setText(context.getString(R.string.info_id) + needies.get(position).getId().toString());
        holder.needyId.setText(context.getString(R.string.info_id_needy)  + needies.get(position).getNeedyID());
        holder.helpInfo.setText(context.getString(R.string.info_help_needy)  + needies.get(position).getHelpInfo());

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(needies.get(position).getHelpDate().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("dd-MM-yy");
        //System.out.println(print.format(parsedDate));
        //System.out.println(parsedDate.toString());
        holder.helpDate.setText(context.getString(R.string.info_needy_help_date) + print.format(parsedDate));

        try {
            parsedDate = sdf.parse(needies.get(position).getSubmitDate().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(print.format(parsedDate));
        //System.out.println(parsedDate.toString());
        holder.submitDate.setText(context.getString(R.string.info_submit_date) + print.format(parsedDate));

        final NeedyViewHolder holderF = holder;
        holder.iv_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, holderF.iv_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                String userNickName = null;
                SharedPreferences mySharedPreferences = context.getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
                if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
                    userNickName = mySharedPreferences.getString("CIVITATI_PREFERENCES", "");
                }
                final String userNickNameF = userNickName;
                final int positionF = position;
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                                Call<ResponseBody> tryToDelete = apiInterface.deleteHNRow(needies.get(positionF).getId(), userNickNameF, "DR" );
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
                                            Toast.makeText(context,context.getString(R.string.report_delete_success), Toast.LENGTH_SHORT).show();
                                            needies.remove(positionF);
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Toast.makeText(context,context.getString(R.string.report_delete_fail), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.i("Civitati", "Error: Record was not deleted" );
                                    }
                                });
                                break;
                            case R.id.update:
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.helpNeedyInfo,  new UpdateHelpNeedyFragment(needies.get(positionF), context));
                                helpNeedyView.findViewById(R.id.addHelpNeedyBtn).setVisibility(View.INVISIBLE);
                                helpNeedyView.findViewById(R.id.rv).setVisibility(View.INVISIBLE);
                                transaction.commit();
                                Toast.makeText(context,context.getString(R.string.report_update_prepare), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    @Override
    public int getItemCount() {
        return needies.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class NeedyViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView needyId;
        TextView needyHelpId;
        TextView assistantNickname;
        TextView helpInfo;
        TextView helpDate;
        TextView submitDate;
        ImageView iv_menu;
        NeedyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.needyList);
            needyHelpId = (TextView)itemView.findViewById(R.id.id);
            needyId = (TextView)itemView.findViewById(R.id.needyID);
            assistantNickname = (TextView)itemView.findViewById(R.id.name);
            helpInfo = (TextView)itemView.findViewById(R.id.helpInfo);
            helpDate = (TextView)itemView.findViewById(R.id.helpDate);
            submitDate = (TextView)itemView.findViewById(R.id.submitDate);
            iv_menu = itemView.findViewById(R.id.iv_menu);

        }
    }
}