package com.app.civitati.ui.dashboard;

import android.content.Context;
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
import com.app.civitati.ui.home.NHAdapter;

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

public class NAdapter extends RecyclerView.Adapter<NAdapter.NeedyViewHolder>{
    List<Needy> needies;
    Context context;
    FragmentManager fragmentManager;
    View helpNeedyView;
    NAdapter(List<Needy> needies, Context context, FragmentManager fragmentManager, View helpNeedyView){
        this.needies = needies;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.helpNeedyView = helpNeedyView;
    }

    @NonNull
    @Override
    public NeedyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.needy_list_row, viewGroup, false);
        NeedyViewHolder nvh = new NeedyViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NeedyViewHolder holder, int position) {
        holder.needyId.setText("ID: " + needies.get(position).getId().toString());
        holder.needyName.setText("Name: " + needies.get(position).getName());
        holder.needyHelpReason.setText("Help Reason: " + needies.get(position).getHelpReason());
        holder.needyAddress.setText("Address: " + needies.get(position).getAdress());
        holder.needyTelephone.setText("Telephone: " + (CharSequence) needies.get(position).getTelephone().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(needies.get(position).getSubmitDate().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("dd-MM-yy");
        //System.out.println(print.format(parsedDate));
        //System.out.println(parsedDate.toString());
        holder.needyDate.setText("Submit date: " + print.format(parsedDate));

        final NAdapter.NeedyViewHolder holderF = holder;
        final int positionF = position;
        holder.iv_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, holderF.iv_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                                Call<ResponseBody> tryToDelete = apiInterface.deleteNRow(needies.get(positionF).getId() , "DR" );
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
                                            needies.remove(positionF);
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
                                break;
                            case R.id.update:
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.dashboard,  new UpdateNeedyFragment(needies.get(positionF), context));
                                helpNeedyView.findViewById(R.id.addNeedyBtn).setVisibility(View.INVISIBLE);
                                helpNeedyView.findViewById(R.id.rv).setVisibility(View.INVISIBLE);
                                transaction.commit();
                                Toast.makeText(context,"You Clicked update ", Toast.LENGTH_SHORT).show();
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
        TextView needyName;
        TextView needyHelpReason;
        TextView needyAddress;
        TextView needyTelephone;
        TextView needyDate;
        ImageView iv_menu;
        NeedyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.needyList);
            needyId = (TextView)itemView.findViewById(R.id.id);
            needyName = (TextView)itemView.findViewById(R.id.name);
            needyHelpReason = (TextView)itemView.findViewById(R.id.needyID);
            needyAddress = (TextView)itemView.findViewById(R.id.helpInfo);
            needyTelephone = (TextView)itemView.findViewById(R.id.helpDate);
            needyDate = (TextView)itemView.findViewById(R.id.submitDate);
            iv_menu = itemView.findViewById(R.id.iv_menu);
        }
    }
}