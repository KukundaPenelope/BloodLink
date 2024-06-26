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
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("patients").whereEqualTo("user_id", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String patientId = patientSnapshot.getString("patient_id");

                        // Query the "requests" collection for requests with status "rejected" and patient_id matching the current user's ID
                        db.collection("requests")
                                .whereEqualTo("status", "rejected")
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


    public void addRequest(Request request) {
        // Not required in Firestore as it automatically updates in real-time
    }

}
