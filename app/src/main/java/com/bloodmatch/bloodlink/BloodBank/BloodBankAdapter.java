package com.bloodmatch.bloodlink.BloodBank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;

import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.BloodBankViewHolder> {
    private List<BloodBanks> bloodBanks;

    public BloodBankAdapter(List<BloodBanks> bloodBanks) {
        this.bloodBanks = bloodBanks;
    }

    @NonNull
    @Override
    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_bank, parent, false);
        return new BloodBankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position) {
        BloodBanks bloodBank = bloodBanks.get(position);
        holder.bind(bloodBank);
    }

    @Override
    public int getItemCount() {
        return bloodBanks.size();
    }

    public static class BloodBankViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView districtTextView;
        private TextView addressTextView;
        private TextView contactTextView;

        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            districtTextView = itemView.findViewById(R.id.districtTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
        }

        public void bind(BloodBanks bloodBank) {
            nameTextView.setText(bloodBank.getName());
            districtTextView.setText(bloodBank.getDistrict());
            addressTextView.setText(bloodBank.getAddress());
            contactTextView.setText(bloodBank.getContact());
        }
    }
}