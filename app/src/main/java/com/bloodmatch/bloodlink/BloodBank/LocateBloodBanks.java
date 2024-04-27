package com.bloodmatch.bloodlink.BloodBank;

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

import com.bloodmatch.bloodlink.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocateBloodBanks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BloodBankAdapter adapter;
    private List<BloodBanks> bloodBanks;
    private EditText searchEditText;
    private FirebaseFirestore db;

    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_blood_banks);

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

        bloodBanks = new ArrayList<>();
        adapter = new BloodBankAdapter(this, bloodBanks);
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
                filterDistricts(district);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void fetchDistricts() {
        db.collection("blood_bank")
                .get()
                .addOnCompleteListener(task ->  {
                        if (task.isSuccessful()) {
                            bloodBanks.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BloodBanks bloodBank = document.toObject(BloodBanks.class);
                                bloodBanks.add(bloodBank);
                            }
                            updateBloodBankAdapter(bloodBanks);
                        } else {
                            Toast.makeText(LocateBloodBanks.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });

    }

    private void filterDistricts(String district) {
        List<BloodBanks> filteredDistricts = new ArrayList<>();
        for (BloodBanks bloodBank : bloodBanks) {
            if (bloodBank.getDistrict().toLowerCase().contains(district.toLowerCase())) {
                filteredDistricts.add(bloodBank);
            }
        }
        updateBloodBankAdapter(filteredDistricts);
    }

    private void updateBloodBankAdapter(List<BloodBanks> bloodBanks) {
        BloodBankAdapter bloodBankAdapter = new BloodBankAdapter(this, bloodBanks);
        recyclerView.setAdapter(bloodBankAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
