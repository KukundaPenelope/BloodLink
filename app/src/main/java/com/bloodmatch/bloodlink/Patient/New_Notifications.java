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

import com.bloodmatch.bloodlink.Donor.RecieveRequestsAdapter;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            db = FirebaseFirestore.getInstance();
            Query query = db.collection("requests")
                    .whereEqualTo("donor_id", currentUserId)
                    .whereEqualTo("status", "pending");

            query.addSnapshotListener((querySnapshot, e) -> {
                if (e != null) {
                    // Handle error
                    return;
                }

                requestList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Request request = document.toObject(Request.class);
                    requestList.add(request);
                }

                adapter.notifyDataSetChanged();
                if (requestList.isEmpty()) {
                    noRequestsTextView.setVisibility(View.VISIBLE);
                } else {
                    noRequestsTextView.setVisibility(View.GONE);
                }
            });
        }

        return rootView;
    }

    public void removeRequest(Request request) {
        requestList.remove(request);
        adapter.notifyDataSetChanged();
    }
}