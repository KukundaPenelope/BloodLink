package com.bloodmatch.bloodlink.Donor;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;

import java.util.List;

public class DistrictSelectionDialog extends Dialog {
    private List<String> districts;
    private DistrictSelectionListener listener;

    public DistrictSelectionDialog(@NonNull Context context, List<String> districts, DistrictSelectionListener listener) {
        super(context);
        this.districts = districts;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_district_selection);

        RecyclerView districtRecyclerView = findViewById(R.id.districtRecyclerView);
        districtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DistrictAdapter adapter = new DistrictAdapter(districts);
        districtRecyclerView.setAdapter(adapter);
    }

    private class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> {
        private List<String> districts;

        DistrictAdapter(List<String> districts) {
            this.districts = districts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_district, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String district = districts.get(position);
            holder.districtTextView.setText(district);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDistrictSelected(district);
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return districts.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView districtTextView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                districtTextView = itemView.findViewById(R.id.districtTextView);
            }
        }
    }

    public interface DistrictSelectionListener {
        void onDistrictSelected(String district);
    }
}