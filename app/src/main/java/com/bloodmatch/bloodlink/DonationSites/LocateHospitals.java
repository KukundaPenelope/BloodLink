package com.bloodmatch.bloodlink.DonationSites;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Hospital.Hospital;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocateHospitals extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HospitalsAdapter adapter;
    private List<Hospital> hospitals;

    private EditText searchEditText;
    private FirebaseFirestore db;

    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_hospitals);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.search_district);
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

        hospitals = new ArrayList<>();
        adapter = new HospitalsAdapter(this, hospitals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchDistricts();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String district = charSequence.toString();
                filterHospitals(district);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void fetchDistricts() {
        db.collection("hospital")
                .get()
                .addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        hospitals.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Hospital hospital = document.toObject(Hospital.class);
                            hospitals.add(hospital);
                        }
                        updateHospital(hospitals);
                    } else {
                        Toast.makeText(LocateHospitals.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void filterHospitals(String district) {
        List<Hospital> filteredHospials = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            if (hospital.getDistrict().toLowerCase().contains(district.toLowerCase())) {
                filteredHospials.add(hospital);
            }
        }
        updateHospital(filteredHospials);
    }

    private void updateHospital(List<Hospital> hospitals) {
        HospitalsAdapter hospitalsAdapter = new HospitalsAdapter(this, hospitals);
        recyclerView.setAdapter(hospitalsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
