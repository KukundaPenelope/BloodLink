package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CancelledNotificationsFragment extends Fragment {

    private TextView noRequestsTextView;
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<Request> requestList;
    private FirebaseFirestore db;

    public static CancelledNotificationsFragment newInstance() {
        return new CancelledNotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cancelled_notifications, container, false);
        recyclerView = rootView.findViewById(R.id.requestsRecyclerView);
        noRequestsTextView = rootView.findViewById(R.id.noRequestsTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RequestAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load requests with status "Cancelled" when the fragment starts
        loadCancelledRequests();
    }

    private void loadCancelledRequests() {
        // Get the ID of the currently logged-in user
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query Firestore for requests with status "Cancelled" and patient ID matching the current user's ID
        Query query = db.collection("requests")
                .whereEqualTo("requestStatus", "cancelled")
                .whereEqualTo("patientId", currentUserId);

        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Handle errors
                return;
            }

            if (queryDocumentSnapshots != null) {
                List<Request> requests = new ArrayList<>();
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        // Add new request to the list
                        Request request = dc.getDocument().toObject(Request.class);
                        requests.add(request);
                    }
                }
                // Update the adapter with the retrieved requests
                adapter.setRequests(requests);
                // Update visibility of no requests message
                if (requests.isEmpty()) {
                    noRequestsTextView.setVisibility(View.VISIBLE);
                } else {
                    noRequestsTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void addRequest(Request request) {
        // Not required in Firestore as it automatically updates in real-time
    }

}
