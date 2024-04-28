package com.bloodmatch.bloodlink.Donor;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.Patient.Request;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CancelledNotifications extends Fragment {

    private TextView noRequestsTextView;
    private RecyclerView recyclerView;
    private RecieveRequestsAdapter adapter;
    private List<Request> requestList;

    private FirebaseFirestore db;

    public static CancelledNotifications newInstance() {
        return new CancelledNotifications();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cancelled_notifications2, container, false);

        noRequestsTextView = rootView.findViewById(R.id.noRequestsTextView);
        recyclerView = rootView.findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestList = new ArrayList<>();
        adapter = new RecieveRequestsAdapter(requestList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();



        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Load requests with status "in_progress" when the fragment starts
        loadRequestsInProgress();
    }
    private void loadRequestsInProgress() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("donors").whereEqualTo("user_id", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot donorSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String donor_id = donorSnapshot.getString("donor_id");

                        // Query the "requests" collection for requests with status "in_progress" and patient_id matching the current user's ID
                        db.collection("requests")
                                .whereEqualTo("status", "rejected")
                                .whereEqualTo("donor_id", donor_id)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        List<Request> requests = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Request request = document.toObject(Request.class);
                                            requests.add(request);
                                        }
                                        // Update the adapter with the retrieved requests
                                        adapter.setRequests(requests);
                                        if (requests.isEmpty()) {
                                            noRequestsTextView.setVisibility(View.VISIBLE);
                                        } else {
                                            noRequestsTextView.setVisibility(View.GONE);
                                        }
                                    } else {
                                        // Handle query failure
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                });
                    }
                });
    }


    public void addRequest(Request request) {
        requestList.add(request);
        adapter.notifyDataSetChanged();
    }
}
