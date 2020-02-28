package com.app.civitati.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.civitati.R;

import java.util.List;

public class NAdapter extends RecyclerView.Adapter<NAdapter.NeedyViewHolder>{
    List<Needy> needies;
    NAdapter(List<Needy> needies){
        this.needies = needies;
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
        holder.needyId.setText(needies.get(position).getId().toString());
        holder.needyName.setText(needies.get(position).getName());
        holder.needyHelpReason.setText(needies.get(position).getHelpReason());
        holder.needyAddress.setText(needies.get(position).getAdress());
        holder.needyTelephone.setText((CharSequence) needies.get(position).getTelephone().toString());
        //holder.needyDate.setText(needies.get(position).getSubmitDate());
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
        NeedyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.needyList);
            needyId = (TextView)itemView.findViewById(R.id.id);
            needyName = (TextView)itemView.findViewById(R.id.name);
            needyHelpReason = (TextView)itemView.findViewById(R.id.helpReason);
            needyAddress = (TextView)itemView.findViewById(R.id.address);
            needyTelephone = (TextView)itemView.findViewById(R.id.telephone);
            needyDate = (TextView)itemView.findViewById(R.id.date);
        }
    }
}