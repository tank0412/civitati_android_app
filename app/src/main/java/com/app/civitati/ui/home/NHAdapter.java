package com.app.civitati.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    NHAdapter(List<NeedyHelp> needies){
        this.needies = needies;
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
        NeedyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.needyList);
            needyHelpId = (TextView)itemView.findViewById(R.id.id);
            needyId = (TextView)itemView.findViewById(R.id.needyID);
            assistantNickname = (TextView)itemView.findViewById(R.id.name);
            helpInfo = (TextView)itemView.findViewById(R.id.helpInfo);
            helpDate = (TextView)itemView.findViewById(R.id.helpDate);
            submitDate = (TextView)itemView.findViewById(R.id.submitDate);
        }
    }
}