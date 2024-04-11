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
import com.bloodmatch.bloodlink.Patient.Request;
import com.bloodmatch.bloodlink.Patient.RequestAdapter;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class New_Notifications extends Fragment {

    private TextView noRequestsTextView;
    private RecyclerView recyclerView;
    private RecieveRequestsAdapter adapter;
    private List<Request> requestList;
    private DatabaseReference requestsRef;

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
            requestsRef = FirebaseDatabase.getInstance().getReference("Requests");
            Query query = requestsRef.orderByChild("donorId").equalTo(currentUserId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request request = snapshot.getValue(Request.class);
                        if (request != null) {
                            if ("Pending".equals(request.getRequestStatus())) {
                                requestList.add(request);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (requestList.isEmpty()) {
                        noRequestsTextView.setVisibility(View.VISIBLE);
                    } else {
                        noRequestsTextView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
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
