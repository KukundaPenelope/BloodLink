package com.bloodmatch.bloodlink.BloodBank;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.Manifest;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
public class LocateBloodBanks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BloodBankAdapter adapter;
    private List<BloodBanks> bloodBanks;
    private EditText searchEditText;

    private DatabaseReference bloodBanksRef;
    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_blood_banks);

        recyclerView = findViewById(R.id.recyclerView);

        searchEditText = findViewById(R.id.search_district);
        backTool = findViewById(R.id.toolbar);
        // Initialize the toolbar
        setSupportActionBar(backTool);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back arrow click
                // You can use onBackPressed() or your custom logic here
                onBackPressed();
            }
        });
        // Set up RecyclerView
        bloodBanks = new ArrayList<>();
//        BloodBan = new ArrayList<>();
//        bloodBanks = getBloodBankListFromDatabase();
//        BloodBan.addAll(bloodBanks);

//
//        adapter = new BloodBankAdapter(this, bloodBanks);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);


        // Set up Firebase reference
        fetchDistricts();
        adapter  = new BloodBankAdapter(this, bloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // Set up TextWatcher for search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Retrieve entered district name
                String district = charSequence.toString();

                // Query the blood banks by district
                filterDistricts(district);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });
    }
//
    public void fetchDistricts() {
        bloodBanks = new ArrayList<>();
        bloodBanksRef = FirebaseDatabase.getInstance().getReference("bloodbanks");
        bloodBanksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bloodBankSnapshot : snapshot.getChildren()) {
                    BloodBanks bloodBank = bloodBankSnapshot.getValue(BloodBanks.class);
                    bloodBanks.add(bloodBank);
                }
                updateBloodBankAdapter(bloodBanks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }
    private void filterDistricts(String district){
        List<BloodBanks> filteredDistricts = new ArrayList<>();
        for (BloodBanks bloodBanks1: bloodBanks) {
            if (bloodBanks1.getDistrict().toLowerCase().contains(district.toLowerCase())) {
                filteredDistricts.add(bloodBanks1);
            }

        }
        updateBloodBankAdapter(filteredDistricts);
    }
    private void updateBloodBankAdapter(List<BloodBanks> bloodBanks){
        BloodBankAdapter bloodBankAdapter = new BloodBankAdapter(this,bloodBanks);
        recyclerView.setAdapter(bloodBankAdapter);
    }



}