package com.bloodmatch.bloodlink.Patient;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AvailablePatientsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AvailablePatientsAdapter adapter;
    private List<Patient> patientList;
    private DatabaseReference database;
    private String selectedBloodGroup;
    private String selectedDistrict;
    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_patients);

        // Retrieve selected blood group and district from the intent
        Intent intent = getIntent();
        selectedBloodGroup = intent.getStringExtra("bloodGroup");
        selectedDistrict = intent.getStringExtra("location");

        recyclerView = findViewById(R.id.recyclerView);
        backTool = findViewById(R.id.toolbar);
        setSupportActionBar(backTool);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientList = new ArrayList<>();
        adapter = new AvailablePatientsAdapter(this, patientList);
        recyclerView.setAdapter(adapter);

        // Get a reference to the Firebase database
        database = FirebaseDatabase.getInstance().getReference("Patients");

        // Query the "Patients" node based on the selected blood group and district
        Query query = database.orderByChild("bloodGroup").equalTo(selectedBloodGroup);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the patient list before adding new data
                patientList.clear();

                // Iterate through the dataSnapshot to retrieve patient data
                for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = patientSnapshot.getValue(Patient.class);
                    if (patient != null && patient.getLocation().equals(selectedDistrict)) {
                        // Add the patient to the list if the location matches the selected district
                        patientList.add(patient);
                    }
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval
                Toast.makeText(AvailablePatientsActivity.this, "Failed to retrieve patients", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
