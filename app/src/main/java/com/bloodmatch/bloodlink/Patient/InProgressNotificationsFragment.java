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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InProgressNotificationsFragment extends Fragment {
    private TextView noRequestsTextView;

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private FirebaseFirestore db;

    public static InProgressNotificationsFragment newInstance() {
        return new InProgressNotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_progress_notifications, container, false);
        noRequestsTextView = view.findViewById(R.id.noRequestsTextView);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RequestAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load requests with status "in_progress" when the fragment starts
        loadRequestsInProgress();
    }

    private void loadRequestsInProgress() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("patients").whereEqualTo("user_id", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String patientId = patientSnapshot.getString("patient_id");

                        // Query the "requests" collection for requests with status "in_progress" and patient_id matching the current user's ID
                        db.collection("requests")
                                .whereEqualTo("status", "pending")
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
