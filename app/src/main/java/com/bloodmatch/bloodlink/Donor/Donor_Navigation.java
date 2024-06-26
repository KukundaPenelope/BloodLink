package com.bloodmatch.bloodlink.Donor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.BloodBank.LocateBloodBanks;
import com.bloodmatch.bloodlink.DonationSites.LocateDonationSites;
import com.bloodmatch.bloodlink.DonationSites.LocateHospitals;
import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.Patient.AboutDonation;
import com.bloodmatch.bloodlink.Patient.AvailablePatientsActivity;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Donor_Navigation extends AppCompatActivity {
    private ArrayAdapter<String> districtAdapter;
    private List<String> districts;
    private TextView donation, viewRequests, aboutDonate;
    private ImageView logout, requestsView, about, receipients, donorsProfile;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_navigation);

        districts = new ArrayList<>();
        districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        donation = findViewById(R.id.donation2);
        viewRequests = findViewById(R.id.viewRequest);
        receipients = findViewById(R.id.receipients);
        requestsView = findViewById(R.id.requestView);
        ImageView donationSitesImageView = findViewById(R.id.donationSites);
         donorsProfile = findViewById(R.id.donorsProfile);
        aboutDonate = findViewById(R.id.aboutdon);
        about = findViewById(R.id.aboutDonation);
        // Initialize the Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        logout = findViewById(R.id.logout);

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
        EditText userNameTextView = findViewById(R.id.name);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve the patient document based on the user ID
            db.collection("donors").whereEqualTo("user_id", userId).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String patientId = patientSnapshot.getId(); // Get the document ID as the patient ID
                            String fname = patientSnapshot.getString("name"); // Assuming "email" is the field storing user's name
                            userNameTextView.setText(fname);
                        } else {
                            userNameTextView.setText("User not authenticated");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
                    });
        }

        aboutDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Donor_Navigation.this, "About Donation", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Donor_Navigation.this, AboutDonation.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Donor_Navigation.this, "About Donation", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Donor_Navigation.this, AboutDonation.class);
                startActivity(intent);
            }
        });
        requestsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Donor_Navigation.this, "View Requests", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Donor_Navigation.this, Donor_Notifications.class);
                startActivity(intent);
            }
        });
        viewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Donor_Navigation.this, "View Requests", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Donor_Navigation.this, Donor_Notifications.class);
                startActivity(intent);
            }
        });
        receipients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Donor_Navigation.this, "View Receipients", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Donor_Navigation.this, LocatePatients.class);
                startActivity(intent);
            }
        });
        donorsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Donor_Navigation.this,donordetails.class);
                startActivity(intent);
            }
        });
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a popup dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Donor_Navigation.this);
                builder.setTitle("Locate Donation Sites");
                builder.setMessage("Do you want to locate hospitals or blood banks?");
                builder.setPositiveButton("Locate Hospitals", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Donor_Navigation.this, LocateHospitals.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Locate Blood Banks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Donor_Navigation.this, LocateDonationSites.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });
//        donation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Donor_Navigation.this, "Locate Donation sites", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Donor_Navigation.this, LocateDonationSites.class);
//                startActivity(intent);
//            }
//        });
        donationSitesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a popup dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Donor_Navigation.this);
                builder.setTitle("Locate Donation Sites");
                builder.setMessage("Do you want to locate hospitals or blood banks?");
                builder.setPositiveButton("Locate Hospitals", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Donor_Navigation.this, LocateHospitals.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Locate Blood Banks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Donor_Navigation.this, LocateDonationSites.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });



//        rewardsImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle donor rewards action
//                // Open a new activity, show rewards information, or perform any other desired action
//            }
//        });

    }

        @Override
        public void onBackPressed () {
            // Navigate back to the previous activity
            super.onBackPressed();
        }
    }

