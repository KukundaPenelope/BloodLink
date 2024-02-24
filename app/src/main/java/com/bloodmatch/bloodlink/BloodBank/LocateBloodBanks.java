package com.bloodmatch.bloodlink.BloodBank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocateBloodBanks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BloodBankAdapter adapter;
    private List<BloodBanks> bloodBanks;
    private EditText searchEditText;

    private DatabaseReference bloodBanksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_blood_banks);

        recyclerView = findViewById(R.id.honorRequest);
        searchEditText = findViewById(R.id.search_district);

        // Set up RecyclerView
        bloodBanks = new ArrayList<>();
        adapter = new BloodBankAdapter(bloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up Firebase reference
        bloodBanksRef = FirebaseDatabase.getInstance().getReference("bloodbanks");

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
                queryBloodBanksByDistrict(district);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });
    }

    private void queryBloodBanksByDistrict(String district) {
        Query query;
        if (district.isEmpty()) {
            query = bloodBanksRef;
        } else {
            query = bloodBanksRef.orderByChild("district").equalTo(district);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodBanks.clear();
                for (DataSnapshot bloodBankSnapshot : dataSnapshot.getChildren()) {
                    BloodBanks bloodBank = bloodBankSnapshot.getValue(BloodBanks.class);
                    bloodBanks.add(bloodBank);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}