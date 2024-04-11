package com.bloodmatch.bloodlink.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Donor.Donor;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<Request> requests;

    public RequestAdapter(List<Request> requests) {
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
        Donor donor = new Donor();
        // Bind data to views in ViewHolder
        holder.requestIdTextView.setText(request.getRequestId());
        DatabaseReference donorRef = FirebaseDatabase.getInstance().getReference("Donors").child(request.getDonorId());
        donorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    if (donor != null) {
                        String bloodGroup = donor.getBloodGroup();
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
        String donorName = "Donor " + (position + 1);
        holder.requestName.setText(donorName);
        holder.requestTimeTextView.setText(request.getRequestTime());
        holder.requestStatusTextView.setText(request.getRequestStatus());
        // Add click listeners or any other UI interactions if needed
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
