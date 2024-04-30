package com.bloodmatch.bloodlink.BloodBank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;

import java.util.List;
import java.util.Map;

public class BloodmatchAdapter extends RecyclerView.Adapter<BloodmatchAdapter.BloodmatchViewHolder> {
    private List<BloodBanks> bloodBanks;
    private Context context;

    public BloodmatchAdapter(Context context, List<BloodBanks> bloodBanks) {
        this.context = context;
        this.bloodBanks = bloodBanks;
    }

    @NonNull
    @Override
    public BloodmatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bloodmatch, parent, false);
        return new BloodmatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodmatchViewHolder holder, int position) {
        BloodBanks bloodBank = bloodBanks.get(position);
        holder.bind(bloodBank);
    }

    @Override
    public int getItemCount() {
        return bloodBanks.size();
    }

    public static class BloodmatchViewHolder extends RecyclerView.ViewHolder {
        private TextView bloodTypeTextView;
        private TextView amountTextView;
        private TextView nameTextView;
        private TextView locationTextView;

        public BloodmatchViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodTypeTextView = itemView.findViewById(R.id.bloodtype);
            amountTextView = itemView.findViewById(R.id.amount);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.addressTextView);
        }

        public void bind(BloodBanks bloodBank) {
            // Get blood type and its amount
            Map<String, Long> bloodAmounts = bloodBank.getBloodAmounts();
            StringBuilder bloodTypes = new StringBuilder();
            StringBuilder amounts = new StringBuilder();
            for (Map.Entry<String, Long> entry : bloodAmounts.entrySet()) {
                bloodTypes.append(entry.getKey()).append("\n");
                amounts.append(entry.getValue()).append("\n");
            }

            // Set text views
            bloodTypeTextView.setText(bloodTypes.toString());
            amountTextView.setText(amounts.toString());
            nameTextView.setText(bloodBank.getName());
            locationTextView.setText(bloodBank.getAddress());
        }
    }
}
