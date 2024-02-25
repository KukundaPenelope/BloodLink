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

import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocateBloodDonors extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private BloodDonorAdapter donorAdapter;
    private List<Donor> donorList;
    private Toolbar backTool;

    DatabaseReference donorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_blood_donors);

        // Initialize Firebase Realtime Database reference
        donorsRef = FirebaseDatabase.getInstance().getReference("Donors");

        // Initialize UI elements
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize donor list
        donorList = new ArrayList<>();

        // Create and set the adapter
        donorAdapter = new BloodDonorAdapter(donorList);
        recyclerView.setAdapter(donorAdapter);

        // Set a text watcher for the search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchedBloodGroup = s.toString().trim();
                filterBloodDonors(searchedBloodGroup);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        fetchAllBloodDonors();

    }

    private void filterBloodDonors(String bloodGroup) {
        if (TextUtils.isEmpty(bloodGroup)) {
            // Show all donors if the search query is empty
            fetchAllBloodDonors();
        } else {
            Query query = donorsRef.orderByChild("bloodGroup").equalTo(bloodGroup);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    donorList.clear();

                    for (DataSnapshot donorSnapshot : dataSnapshot.getChildren()) {
                        Donor donor = donorSnapshot.getValue(Donor.class);
                        if (donor != null) {
                            donorList.add(donor);
                        }
                    }

                    donorAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private void fetchAllBloodDonors() {
        donorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donorList.clear();

                for (DataSnapshot donorSnapshot : dataSnapshot.getChildren()) {
                    Donor donor = donorSnapshot.getValue(Donor.class);
                    if (donor != null) {
                        donorList.add(donor);
                    }
                }

                donorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}