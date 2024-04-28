package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
import com.bloodmatch.bloodlink.Patient.BloodBankAdapter2;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Find_A_Blood_Match extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BloodBankAdapter2 bloodBankAdapter;
    private List<BloodBanks> bloodBanksList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ablood_match);

        recyclerView = findViewById(R.id.recyclerView);
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
                            String patientLocation = documentSnapshot.getString("location");
                            // Retrieve blood banks based on patient's blood group and location
                            retrieveBloodBanks(patientBloodGroup, patientLocation);
                        }
                    } else {
                        Toast.makeText(Find_A_Blood_Match.this, "No patient data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Find_A_Blood_Match.this, "Failed to retrieve patient data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to retrieve blood banks based on patient's blood group and location
    private void retrieveBloodBanks(String patientBloodGroup, String patientLocation) {
        db.collection("blood_bank").whereEqualTo("district", patientLocation)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bloodBanksList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        BloodBanks bloodBank = document.toObject(BloodBanks.class);
                        Map<String, Object> bloodAmount = bloodBank.getBloodAmount();
                        String requiredBloodQuantity = getBloodQuantity(patientBloodGroup, bloodAmount);
                        if (requiredBloodQuantity != null) {
                            int availableBloodQuantity = Integer.parseInt(requiredBloodQuantity);
                            if (availableBloodQuantity >= 0) {
                                bloodBanksList.add(bloodBank);
                            }
                        }
                    }
                    bloodBankAdapter = new BloodBankAdapter2(this, bloodBanksList,patientBloodGroup);
                    recyclerView.setAdapter(bloodBankAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Find_A_Blood_Match.this, "Failed to retrieve blood banks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to get the blood quantity based on the patient's blood group
    private String getBloodQuantity(String patientBloodGroup, Map<String, Object> bloodAmount) {
        switch (patientBloodGroup) {
            case "A+":
                return bloodAmount.get("A+").toString();
            case "A-":
                return bloodAmount.get("A-").toString();
            case "B+":
                return bloodAmount.get("B+").toString();
            case "B-":
                return bloodAmount.get("B-").toString();
            case "AB+":
                return bloodAmount.get("AB+").toString();
            case "AB-":
                return bloodAmount.get("AB-").toString();
            case "O+":
                return bloodAmount.get("O+").toString();
            case "O-":
                return bloodAmount.get("O-").toString();
            default:
                return null;
        }
    }
}