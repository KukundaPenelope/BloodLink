package com.bloodmatch.bloodlink.Donor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.Patient.Request;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecieveRequestsAdapter extends RecyclerView.Adapter<RecieveRequestsAdapter.ViewHolder> {

    private List<Request> requests;
    FirebaseFirestore db;

    private ViewHolder holder;

    public RecieveRequestsAdapter(List<Request> requests) {
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
        // Add click listeners or any other UI interactions if needed

        // Retrieve patient details based on the patientId in the request
        db.collection("patients").document(request.getPatient_id()).get()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
//                Patient patient = task.toObject(Patient.class);
                Patient patient = documentSnapshot.toObject(Patient.class);
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Retrieve patient attributes
                    String firstName = patient.getFirst_name();
                    String lastName = patient.getLast_name();
                    String fullName = firstName + " " + lastName;
                    String bloodGroup = patient.getBlood_type();
                    String location = documentSnapshot.getString("hospital");
                    // Set patient details to the views in ViewHolder
                    holder.requestName.setText(fullName);
                    holder.donorGroup.setText(bloodGroup);
                    holder.locationTextView.setText(location);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Show the dialog fragment
                            ApprovalDialogFragment dialogFragment = new ApprovalDialogFragment(request, position);
                            dialogFragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "ApprovalDialogFragment");
                        }
                    });

                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView requestIdTextView,locationTextView;
        TextView donorGroup;
        TextView requestName;
        TextView requestTimeTextView;
        TextView requestStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views here
            requestIdTextView = itemView.findViewById(R.id.requestIdTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
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
