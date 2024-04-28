package com.bloodmatch.bloodlink.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Donor.Donor;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Request> requests;
    private FirebaseFirestore db;

    public RequestAdapter(List<Request> requests) {
        this.requests = requests;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = requests.get(position);

        // Bind data to views in ViewHolder
        holder.requestIdTextView.setText(request.getRequest_id());
        holder.requestTimeTextView.setText(request.getRequest_time());
        holder.requestStatusTextView.setText(request.getStatus());
        Donor donor = new Donor();
        // Load donor data from Firestore based on donorId
        db.collection("donors").document(request.getDonor_id()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Retrieve donor data
//                            String name = document.getString("name");
                            String bloodGroup = document.getString("blood_type");
                            String blood_group= donor.getBlood_type();
//                            String location = document.getString("location");

                            // Set donor data to the views in ViewHolder
//                            holder.donorNameTextView.setText(name);
                            holder.donorGroup.setText(bloodGroup);
//                            holder.locationTextView.setText(location);
                            // Set the donor information in the item layout
                            String donorName = "Donor " + (position + 1);
                            holder.requestIdTextView.setText(donorName);
                            holder.requestTimeTextView.setText(request.getRequest_time());
                            holder.requestStatusTextView.setText(request.getStatus());
                        } else {
                        }
                    } else {
                        // Handle errors
                    }
                });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView requestIdTextView, donorNameTextView, locationTextView;
        TextView donorGroup;
        TextView requestTimeTextView;
        TextView requestStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views here
            donorNameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.districtTextView);
            requestIdTextView = itemView.findViewById(R.id.requestIdTextView);
            donorGroup = itemView.findViewById(R.id.bloodGroupText);
            requestTimeTextView = itemView.findViewById(R.id.requestTimeTextView);
            requestStatusTextView = itemView.findViewById(R.id.requestStatusTextView);
        }
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }
}
