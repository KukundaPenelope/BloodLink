package com.bloodmatch.bloodlink.BloodBank;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Find_A_Blood_Match extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BloodmatchAdapter adapter;
    private List<BloodBanks> bloodBanks;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ablood_match3);

        recyclerView = findViewById(R.id.recyclerView1);
        backTool = findViewById(R.id.toolbar1);
        setSupportActionBar(backTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bloodBanks = new ArrayList<>();
        adapter = new BloodmatchAdapter(this, bloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Fetch blood banks based on the patient's blood type
        fetchPatientBloodType();
    }

    private void fetchPatientBloodType() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("patients")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String patientId = patientSnapshot.getId();
                            db.collection("patients")
                                    .document(patientId)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String patientBloodType = document.getString("blood_type");
                                                if (patientBloodType != null) {
                                                    fetchBloodBanks(patientBloodType);
                                                } else {
                                                    Toast.makeText(Find_A_Blood_Match.this, "Patient's blood type not found.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(Find_A_Blood_Match.this, "Patient's data not found.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(Find_A_Blood_Match.this, "Error fetching patient's data: " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Find_A_Blood_Match.this, "Patient's data not found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Find_A_Blood_Match.this, "Error fetching patient's data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchBloodBanks(String patientBloodType) {
        db.collection("blood_bank")
                .whereGreaterThan("blood_amount." + patientBloodType, 0)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<BloodBanks> bloodBanks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BloodBanks bloodBank = document.toObject(BloodBanks.class);
                            bloodBanks.add(bloodBank);
                        }
                        updateBloodBankAdapter(bloodBanks);
                    } else {
                        Toast.makeText(Find_A_Blood_Match.this, "Error getting blood banks: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBloodBankAdapter(List<BloodBanks> bloodBanks) {
        BloodmatchAdapter bloodmatchAdapter = new BloodmatchAdapter(this, bloodBanks);
        recyclerView.setAdapter(bloodmatchAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
