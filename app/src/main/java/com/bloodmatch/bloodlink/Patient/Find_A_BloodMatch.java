package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.BloodBank.BloodAmount;
import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Find_A_BloodMatch extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BloodBankAdapter2 bloodBankAdapter;
    private List<BloodBanks> bloodBanksList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ablood_match);

        recyclerView = findViewById(R.id.bloodmatchRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bloodBanksList = new ArrayList<>();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Retrieve the currently logged-in user's UID
            String currentUserId = currentUser.getUid();
            // Retrieve patient's details based on UID
            retrievePatientDetails(currentUserId);
        }
    }

    // Method to retrieve patient's details from Firestore
    private void retrievePatientDetails(String userId) {
        db.collection("patients").whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get patient data
                            String patientBloodGroup = documentSnapshot.getString("blood_type");
                            String hospitalId = documentSnapshot.getString("hospital_id");
                            // Retrieve hospital location based on hospital ID
                            retrieveHospitalLocation(patientBloodGroup, hospitalId);
                        }
                    } else {
                        Toast.makeText(Find_A_BloodMatch.this, "No patient data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Find_A_BloodMatch.this, "Failed to retrieve patient data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to retrieve hospital's location
    private void retrieveHospitalLocation(String patientBloodGroup, String hospitalId) {
        db.collection("hospital").document(hospitalId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String hospitalLocation = documentSnapshot.getString("district");
                        if (hospitalLocation != null) {
                            // Retrieve nearby blood banks with matching blood group and required blood quantities
                            retrieveBloodBanks(patientBloodGroup, hospitalLocation);
                        } else {
                            Toast.makeText(Find_A_BloodMatch.this, "Hospital location not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Find_A_BloodMatch.this, "Hospital document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Find_A_BloodMatch.this, "Failed to retrieve hospital location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to retrieve blood banks based on the hospital's location and patient's blood group
    // Method to retrieve blood banks based on the hospital's location and patient's blood group
    private void retrieveBloodBanks(String patientBloodGroup, String hospitalLocation) {
        bloodBankAdapter = new BloodBankAdapter2(this, bloodBanksList, patientBloodGroup);
        recyclerView.setAdapter(bloodBankAdapter);

        // Query nearby blood banks with matching blood group and required blood quantities
        db.collection("blood_bank").whereEqualTo("district", hospitalLocation)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        BloodBanks bloodBank = document.toObject(BloodBanks.class);

                        // Access the corresponding blood quantity based on the patient's blood group
                        BloodAmount bloodAmount = bloodBank.getBloodAmount();
                        String requiredBloodQuantity = getBloodQuantity(patientBloodGroup, bloodAmount);
                        if (requiredBloodQuantity != null) {
                            // Check if the blood bank has sufficient blood quantity
                            int availableBloodQuantity = Integer.parseInt(requiredBloodQuantity);
                            if (availableBloodQuantity >= 0) {
                                bloodBanksList.add(bloodBank);
                            }
                        }
                    }

                    // Notify the adapter of the data change to update the RecyclerView
                    bloodBankAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Find_A_BloodMatch.this, "Failed to retrieve blood banks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to get blood quantity for the patient's blood group
    private String getBloodQuantity(String patientBloodGroup, BloodAmount bloodAmount) {
        // Access the corresponding blood quantity based on the patient's blood group
        switch (patientBloodGroup) {
            case "A+":
                return bloodAmount.getAPlus();
            case "A-":
                return bloodAmount.getAMinus();
            case "B+":
                return bloodAmount.getBPlus();
            case "B-":
                return bloodAmount.getBMinus();
            case "AB+":
                return bloodAmount.getABPlus();
            case "AB-":
                return bloodAmount.getABMinus();
            case "O+":
                return bloodAmount.getOPlus();
            case "O-":
                return bloodAmount.getOMinus();
            default:
                return null;
        }
    }
}