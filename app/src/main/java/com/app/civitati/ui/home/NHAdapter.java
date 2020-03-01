package com.app.civitati.ui.home;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.app.civitati.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NHAdapter extends RecyclerView.Adapter<NHAdapter.NeedyViewHolder>{
    List<NeedyHelp> needies;
    Context context;
    NHAdapter(List<NeedyHelp> needies,  Context context){
        this.needies = needies;
        this.context = context;
    }

    @NonNull
    @Override
    public NeedyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.needy_help_list_row, viewGroup, false);
        NeedyViewHolder nvh = new NeedyViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NeedyViewHolder holder, int position) {
        holder.needyHelpId.setText("Help ID: " + needies.get(position).getId().toString());
        holder.needyId.setText("Needy ID: " + needies.get(position).getNeedyID());
        holder.helpInfo.setText("Help info: " + needies.get(position).getHelpInfo());

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
        holder.helpDate.setText("Help Date: " + print.format(parsedDate));

        try {
            parsedDate = sdf.parse(needies.get(position).getSubmitDate().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(print.format(parsedDate));
        //System.out.println(parsedDate.toString());
        holder.submitDate.setText("Submit Date: " + print.format(parsedDate));

        final NeedyViewHolder holderF = holder;
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
                                Toast.makeText(context,"You Clicked delete ", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.update:
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