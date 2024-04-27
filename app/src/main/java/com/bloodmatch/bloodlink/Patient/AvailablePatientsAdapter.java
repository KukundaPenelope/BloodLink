package com.bloodmatch.bloodlink.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;

import java.util.ArrayList;
import java.util.List;

public class AvailablePatientsAdapter extends RecyclerView.Adapter<AvailablePatientsAdapter.ViewHolder> {
    private Context context;
    private List<Patient> patientList;
    private List<Patient> filteredList;  // New filtered list for search functionality

    public AvailablePatientsAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
        this.filteredList = new ArrayList<>(patientList);  // Initialize filtered list with all patients
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient patient = filteredList.get(position);  // Get patient from filtered list
        holder.nameTextView.setText(patient.getFirst_name() + " " + patient.getLast_name());
        holder.ageTextView.setText("Age: " + patient.getAge());
        holder.bloodGroupTextView.setText("Blood Group: " + patient.getBlood_type());
        holder.districtTextView.setText("District: " + patient.getLocation());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();  // Use filtered list size for item count
    }

//    public void filterByDistrict(String district) {
//        filteredList.clear();  // Clear the current filtered list
//
//        if (district.isEmpty()) {
//            filteredList.addAll(patientList);  // If search query is empty, show all patients
//        } else {
//            for (Patient patient : patientList) {
//                if (patient.getLocation().equalsIgnoreCase(district)) {
//                    filteredList.add(patient);  // Add patients matching the district to the filtered list
//                }
//            }
//        }
//
//        notifyDataSetChanged();  // Notify adapter about the data change
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView ageTextView;
        public TextView bloodGroupTextView;
        public TextView districtTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            districtTextView = itemView.findViewById(R.id.districtTextView);
        }
    }
}