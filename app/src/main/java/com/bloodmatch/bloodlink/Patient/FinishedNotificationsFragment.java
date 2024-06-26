package com.bloodmatch.bloodlink.Patient;

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

import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FinishedNotificationsFragment extends Fragment {

    private TextView noRequestsTextView;
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<Request> finishedRequests;
    private FirebaseFirestore db;

    public static FinishedNotificationsFragment newInstance() {
        return new FinishedNotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finished_notifications, container, false);
        recyclerView = rootView.findViewById(R.id.requestsRecyclerView);
        noRequestsTextView = rootView.findViewById(R.id.noRequestsTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        finishedRequests = new ArrayList<>();
        adapter = new RequestAdapter(finishedRequests);
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load requests with status "Approved" when the fragment starts
        loadFinishedRequests();
    }
    private void loadFinishedRequests() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("patients").whereEqualTo("user_id", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String patientId = patientSnapshot.getString("patient_id");

                        // Query the "requests" collection for requests with status "in_progress" and patient_id matching the current user's ID
                        db.collection("requests")
                                .whereEqualTo("status", "approved")
                                .whereEqualTo("patient_id", patientId)
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

//    private void loadFinishedRequests() {
//        // Get the ID of the currently logged-in user
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Query Firestore for requests with status "Approved" and patient ID matching the current user's ID
//        Query query = db.collection("requests")
//                .whereEqualTo("requestStatus", "approved")
//                .whereEqualTo("patientId", currentUserId);
//
//        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
//            if (e != null) {
//                // Handle errors
//                return;
//            }
//
//            if (queryDocumentSnapshots != null) {
//                List<Request> requests = new ArrayList<>();
//                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
//                    if (dc.getType() == DocumentChange.Type.ADDED) {
//                        // Add new request to the list
//                        Request request = dc.getDocument().toObject(Request.class);
//                        requests.add(request);
//                    }
//                }
//                // Update the adapter with the retrieved requests
//                adapter.setRequests(requests);
//                // Update visibility of no requests message
//                if (requests.isEmpty()) {
//                    noRequestsTextView.setVisibility(View.VISIBLE);
//                } else {
//                    noRequestsTextView.setVisibility(View.GONE);
//                }
//            }
//        });
//    }

    public void addRequest(Request request) {
        // Not required in Firestore as it automatically updates in real-time
    }

}
