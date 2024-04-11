package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InProgressNotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private DatabaseReference requestsRef;

    public static InProgressNotificationsFragment newInstance() {
        return new InProgressNotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_progress_notifications, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RequestAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        requestsRef = FirebaseDatabase.getInstance().getReference("Requests");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load requests with status "in_progress" when the fragment starts
        loadRequestsInProgress();
    }

    private void loadRequestsInProgress() {
        // Get the ID of the currently logged-in user
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query the database for requests with status "Pending" and donor ID matching the current user's ID
        requestsRef.orderByChild("requestStatus").equalTo("Pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Request> requests = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request = snapshot.getValue(Request.class);
                    if (request != null && request.getPatientId().equals(currentUserId)) {
                        requests.add(request);
                    }
                }
                // Update the adapter with the retrieved requests
                adapter.setRequests(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

}
