package com.bloodmatch.bloodlink.Donor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bloodmatch.bloodlink.BloodBank.LocateBloodBanks;
import com.bloodmatch.bloodlink.DonationSites.LocateDonationSites;
import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.Patient.AvailablePatientsActivity;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Donor_Navigation extends AppCompatActivity {
    private Spinner districtSpinner;
    private ArrayAdapter<String> districtAdapter;
    private List<String> districts;
    private Spinner bloodGroupSpinner;
    private DatabaseReference patientsRef;
    private Button find;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_navigation);

        districts = new ArrayList<>();
        districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner = findViewById(R.id.districtEditText);
        ImageView donationSitesImageView = findViewById(R.id.donationSites);
        ImageView donorsProfileImageView = findViewById(R.id.donorsProfile);
        ImageView rewardsImageView = findViewById(R.id.rewards);
        districtSpinner.setAdapter(districtAdapter);
        districts.add("Select District");
        districtAdapter.notifyDataSetChanged();

        logout= findViewById(R.id.logout);
//
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Donor_Navigation.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear user type from SharedPreferences
                        SharedPreferences.Editor editor = getSharedPreferences("BloodSync", MODE_PRIVATE).edit();
                        editor.remove("userType");
                        editor.apply();

                        // Navigate to the MainActivity2 or login screen
                        Intent intent = new Intent(Donor_Navigation.this, MainActivity3.class);
                        startActivity(intent);
                        finish(); // Close current activity
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if "No" is clicked
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        donationSitesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Donor_Navigation.this, "Locate Donation sites", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Donor_Navigation.this, LocateDonationSites.class);
                startActivity(intent);
            }
        });

        donorsProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle donor's profile action
                // Open a new activity, show user profile information, or perform any other desired action
            }
        });

        rewardsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle donor rewards action
                // Open a new activity, show rewards information, or perform any other desired action
            }
        });


        find = findViewById(R.id.find_recipients_button);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectionValid()) {
                    // Retrieve selected items from spinners
                    String selectedDistrict = districtSpinner.getSelectedItem().toString();
                    String selectedBloodGroup = bloodGroupSpinner.getSelectedItem().toString();

                    // Proceed to next activity
                    Intent intent = new Intent(Donor_Navigation.this, AvailablePatientsActivity.class);
                    intent.putExtra("location", selectedDistrict);
                    intent.putExtra("bloodGroup", selectedBloodGroup);
                    startActivity(intent);
                } else {
                    Toast.makeText(Donor_Navigation.this, "Please select both district and blood group", Toast.LENGTH_SHORT).show();
                }
            }
        });

// Set up the blood group spinner with a prompt and placeholder
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodg_array, android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Create a new list with placeholder
        List<CharSequence> bloodGroupList = new ArrayList<>();
        bloodGroupList.add("Select Blood Group");
        bloodGroupList.addAll(Arrays.asList(getResources().getStringArray(R.array.bloodg_array)));

// Create a new ArrayAdapter with the updated list
        ArrayAdapter<CharSequence> updatedBloodGroupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bloodGroupList);
        updatedBloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(updatedBloodGroupAdapter);

// Set selection to the placeholder
        bloodGroupSpinner.setSelection(0, false);


        // Get a reference to the Firebase database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Retrieve the patients node from the database
        DatabaseReference patientsRef = database.child("Patients");

        // Add a ValueEventListener to retrieve the patient data
        patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                districts.clear(); // Clear existing districts (including placeholder)
                districts.add("Select District");

                // Iterate through the dataSnapshot to retrieve patient data
                for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                    // Get the district property from each patient
                    String district = patientSnapshot.child("location").getValue(String.class);

                    // Add the district to the list if it is not null
                    if (district != null) {
                        districts.add(district);
                    }
                }

                // Notify the adapter that the data set has changed
                districtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval
                Toast.makeText(Donor_Navigation.this, "Failed to retrieve districts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to check if selection in both spinners is valid
    private boolean isSelectionValid() {
        String selectedDistrict = districtSpinner.getSelectedItem().toString();
        String selectedBloodGroup = bloodGroupSpinner.getSelectedItem().toString();
        return !TextUtils.isEmpty(selectedDistrict) && !selectedDistrict.equals("Select District") &&
                !TextUtils.isEmpty(selectedBloodGroup) && !selectedBloodGroup.equals("Select Blood Group");
    }
    @Override
    public void onBackPressed() {
        // Navigate back to the previous activity
        super.onBackPressed();
    }
}
