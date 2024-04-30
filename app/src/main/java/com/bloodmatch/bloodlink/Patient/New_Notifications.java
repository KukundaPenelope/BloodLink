package com.bloodmatch.bloodlink.Patient;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import android.content.Intent;
import android.net.Uri;
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

import com.bloodmatch.bloodlink.Donor.RecieveRequestsAdapter;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class New_Notifications extends Fragment {

    private TextView noRequestsTextView;
    private RecyclerView recyclerView;
    private RecieveRequestsAdapter adapter;
    private List<Request> requestList;
    private FirebaseFirestore db;

    public static New_Notifications newInstance() {
        return new New_Notifications();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new__notifications, container, false);

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
                                .whereEqualTo("status", "pending")
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

    public void removeRequest(Request request) {
        requestList.remove(request);
        adapter.notifyDataSetChanged();

        // Get patient ID from the request object
        String patientId = request.getPatient_id();

        // Check if patient ID is available
        if (patientId != null && !patientId.isEmpty()) {
            // Get a reference to the Firestore database
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Query the "patients" collection for the document matching the patient ID
            db.collection("patients").document(patientId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Extract patient location from the document (assuming it has a location field)
                                    String patientLocation = document.getString("location");

                                    // Check if patient location is available
                                    if (patientLocation != null && !patientLocation.isEmpty()) {
                                        // Launch Google Maps with the patient location
                                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + patientLocation);
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                            startActivity(mapIntent);
                                        } else {
                                            Log.w("New_Notifications", "Google Maps app not found");
                                        }
                                    } else {
                                        Log.w("New_Notifications", "Patient location not available in patient data");
                                    }
                                } else {
                                    Log.w("New_Notifications", "Patient document not found");
                                }
                            } else {
                                Log.w("New_Notifications", "Error getting patient data: ", task.getException());
                            }
                        }
                    });
        } else {
            Log.w("New_Notifications", "Patient ID not available in request");
        }
    }

}