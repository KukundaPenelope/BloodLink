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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecieveRequestsAdapter extends RecyclerView.Adapter<RecieveRequestsAdapter.ViewHolder> {

    private List<Request> requests;

    public RecieveRequestsAdapter(List<Request> requests) {
        this.requests = requests;
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
        holder.requestIdTextView.setText(request.getRequestId());
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("Patients");
        patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Patient patient = snapshot.getValue(Patient.class);
                    if (patient != null) {
                        // Retrieve patient attributes
                        String firstName = patient.getFirstName();
                        String lastName = patient.getLastName();
                        String fullName = firstName + " " + lastName;
                        String bloodGroup = patient.getBloodGroup();

                        // Set data to the views in ViewHolder
                        holder.requestName.setText(fullName);
                        holder.donorGroup.setText(bloodGroup);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Set the donor information in the item layout
        holder.requestTimeTextView.setText(request.getRequestTime());
        holder.requestStatusTextView.setText(request.getRequestStatus());
        // Add click listeners or any other UI interactions if needed
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog fragment
                ApprovalDialogFragment dialogFragment = new ApprovalDialogFragment(request, position);


                dialogFragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "ApprovalDialogFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView requestIdTextView;
        TextView donorGroup;
        TextView requestName;
        TextView requestTimeTextView;
        TextView requestStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views here
            requestIdTextView = itemView.findViewById(R.id.requestIdTextView);
            requestName = itemView.findViewById(R.id.donorNameTextView);
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
