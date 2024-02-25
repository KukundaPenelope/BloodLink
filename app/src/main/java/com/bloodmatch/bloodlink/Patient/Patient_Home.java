package com.bloodmatch.bloodlink.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bloodmatch.bloodlink.BloodBank.LocateBloodBanks;
import com.bloodmatch.bloodlink.BloodBank.avail_bloodbanks;
import com.bloodmatch.bloodlink.Donor.LocateBloodDonors;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Patient_Home extends Fragment {

    private EditText nameEditText;
    private DatabaseReference usersRef;
    CardView findBloodMatch,locate,know, about;

    public Patient_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient__home, container, false);
        findBloodMatch = rootView.findViewById(R.id.donor);
        locate = rootView.findViewById(R.id.locat);
        know = rootView.findViewById(R.id.knows);
        about = rootView.findViewById(R.id.abouts);

        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users").child("Patients");

        // Get the current user's ID from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String patientId = currentUser.getUid();

            usersRef.child(patientId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fname = dataSnapshot.child("firstname").getValue(String.class);
                        String lname = dataSnapshot.child("lastname").getValue(String.class);

                        String fullName = fname + " " + lname;
                        nameEditText.setText(fullName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            nameEditText.setText("User not authenticated");
        }

        findBloodMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocateBloodDonors.class);
                startActivity(intent);
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocateBloodBanks.class);
                startActivity(intent);
            }
        });
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocateBloodBanks.class);
                startActivity(intent);            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocateBloodBanks.class);
                startActivity(intent);
            }
        });

        // Rest of your code...

        return rootView;
    }
}
