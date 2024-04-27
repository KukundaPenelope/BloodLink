package com.bloodmatch.bloodlink.Donor;

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

import com.bloodmatch.bloodlink.Patient.Request;
import com.bloodmatch.bloodlink.Patient.RequestAdapter;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        View rootView = inflater.inflate(R.layout.fragment_cancelled_notifications2, container, false);

        noRequestsTextView = rootView.findViewById(R.id.noRequestsTextView);
        recyclerView = rootView.findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Query Firestore for cancelled requests for the current donor
            db.collection("requests")
                    .whereEqualTo("donorId", currentUserId)
                    .whereEqualTo("requestStatus", "cancelled")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            requestList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Request request = document.toObject(Request.class);
                                requestList.add(request);
                            }
                            adapter.notifyDataSetChanged();
                            if (requestList.isEmpty()) {
                                noRequestsTextView.setVisibility(View.VISIBLE);
                            } else {
                                noRequestsTextView.setVisibility(View.GONE);
                            }
                        } else {
                            // Handle task failure
                        }
                    });
        }

        return rootView;
    }

    public void addRequest(Request request) {
        requestList.add(request);
        adapter.notifyDataSetChanged();
    }
}
