package com.bloodmatch.bloodlink.Donor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.Patient.Request;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocatePatients extends AppCompatActivity  {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private PatientsAdapter patientsadapter;
    private List<Patient> patientsList;
    private Toolbar backTool;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference requestsCollection = db.collection("patients");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_patients);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Initialize UI elements
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);
        backTool = findViewById(R.id.toolbar);
        setSupportActionBar(backTool);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        patientsList = new ArrayList<>();

        patientsadapter = new PatientsAdapter(patientsList, this);
        recyclerView.setAdapter(patientsadapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchedBloodGroup = s.toString().trim();
                filterBloodDonors(searchedBloodGroup);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fetchAllBloodDonors();
    }

    private void filterBloodDonors(String bloodGroup) {
        if (TextUtils.isEmpty(bloodGroup)) {
            fetchAllBloodDonors();
        } else {
//            db.collection("hospital").document().getId();
            Query query = db.collection("patients").whereEqualTo("blood_type", bloodGroup);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    patientsList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Patient patient = document.toObject(Patient.class);
                        patient.setBlood_type(document.getString("blood_type"));
                        patient.setPhone_number(document.getString("phone_number"));
                        patient.setHospital_id(document.getString("location"));
//
                        patientsList.add(patient);
                    }
                    patientsadapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LocatePatients.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchAllBloodDonors() {
        db.collection("patients").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        patientsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Patient patient = document.toObject(Patient.class);
                            patient.setBlood_type(document.getString("blood_type"));
                            patient.setPhone_number(document.getString("phone_number"));
                            patient.setLocation(document.getString("location"));

                            patientsList.add(patient);
                        }
                        patientsadapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LocatePatients.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }









    private String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.getDefault());
        return sdf.format(date);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
