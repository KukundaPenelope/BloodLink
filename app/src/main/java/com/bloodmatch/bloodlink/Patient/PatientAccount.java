package com.bloodmatch.bloodlink.Patient;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.Donor.LocateBloodDonors;
import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientAccount extends Fragment {

    private TextView log;
    private ImageView edit;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;


    public PatientAccount() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_account, container, false);

        log = rootView.findViewById(R.id.logout);

        edit = rootView.findViewById(R.id.editProfile);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        TextView userNameTextView = rootView.findViewById(R.id.usernameTextView);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve the patient document based on the user ID
            db.collection("patients").whereEqualTo("user_id", userId).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String patientId = patientSnapshot.getId(); // Get the document ID as the patient ID
                            String fname = patientSnapshot.getString("email"); // Assuming "email" is the field storing user's name
                            userNameTextView.setText(fname);
                        } else {
                            userNameTextView.setText("User not authenticated");
                        }
                    })
                    .addOnFailureListener(e -> {
                    });
        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
            }
        });

        // Handle logout button click
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login activity
                Intent intent = new Intent(getActivity(), MainActivity3.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish(); // Finish the current activity
            }
        });

        return rootView;
    }
}
