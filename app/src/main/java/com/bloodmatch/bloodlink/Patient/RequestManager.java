package com.bloodmatch.bloodlink.Patient;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestManager {
    private DatabaseReference requestsRef;

    public RequestManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        requestsRef = database.getReference("requests");
    }

    public void saveRequest(Request request) {
        String requestId = requestsRef.push().getKey();
        request.setRequestId(requestId);
        requestsRef.child(requestId).setValue(request);
    }
}