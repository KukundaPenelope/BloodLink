package com.bloodmatch.bloodlink.Patient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Donor.Donor;
import com.bloodmatch.bloodlink.R;

import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {
    private Context context;
    private List<Donor> bloodDonorsList;

    public BloodDonorAdapter(Context context, List<Donor> bloodDonorsList) {
        this.context = context;
        this.bloodDonorsList = bloodDonorsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_matcg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donor bloodDonor = bloodDonorsList.get(position);
        holder.nameTextView.setText(bloodDonor.getName());
        holder.bloodGroupTextView.setText(bloodDonor.getBlood_type());
        holder.locationTextView.setText(bloodDonor.getLocation());
        holder.contactTextView.setText(bloodDonor.getPhone_number());
    }

    @Override
    public int getItemCount() {
        return bloodDonorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, bloodGroupTextView, locationTextView, contactTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
        }
    }
}