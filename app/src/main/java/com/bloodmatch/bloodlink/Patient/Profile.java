package com.bloodmatch.bloodlink.Patient;

import static com.bloodmatch.bloodlink.Patient.Profile2.REQUEST_EXTERNAL_STORAGE_PERMISSION;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bloodmatch.bloodlink.Donor.Donor;
import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText bloodGroup;

    private Button editBtn, logoutBtn, saveBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phoneholder);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        bloodGroup = findViewById(R.id.bloodg);
        logoutBtn = findViewById(R.id.logout);
        editBtn = findViewById(R.id.edit);
        saveBtn = findViewById(R.id.save);
        saveBtn.setVisibility(View.GONE);
        backTool = findViewById(R.id.profile_toolbar);

        // Initialize the Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set toolbar as support action bar
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

        // Check for external storage permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditing();
                updateUserData(nameEditText.getText().toString(), phoneEditText.getText().toString());
            }
        });

        // Retrieve and fill user data
        retrieveAndFillUserData();
    }

    private void retrieveAndFillUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
       String userid= currentUser.getUid();
        Patient patient = new Patient();
        // Load donor data from Firestore based on donorId
        db.collection("patients").whereEqualTo("user_id",userid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (document != null && !document.isEmpty()) {
                            // Retrieve donor data
//                            String name = document.getString("name");

                                String fullName = patient.getFirst_name() + " " + patient.getLast_name();
                                nameEditText.setText(fullName);
                                phoneEditText.setText(patient.getPhone_number());
                                emailEditText.setText(patient.getEmail());
                                passwordEditText.setText(patient.getPassword());
                                bloodGroup.setText(patient.getBlood_type());

                        } else {
                        }
                    } else {
                        // Handle errors
                    }
                });
//        if (currentUser != null) {
//            db.collection("patients").document(currentUser.getUid())
//
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            Patient patient = documentSnapshot.toObject(Patient.class);
//
//                            if (patient != null) {
//                                String fullName = patient.getFirst_name() + " " + patient.getLast_name();
//                                nameEditText.setText(fullName);
//                                phoneEditText.setText(patient.getPhone_number());
//                                emailEditText.setText(patient.getEmail());
//                                passwordEditText.setText(patient.getPassword());
//                                bloodGroup.setText(patient.getBlood_type());
//                            }
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle errors
//                    });
//        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel logout
            }
        });

        builder.show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("patients", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(Profile.this, MainActivity3.class));
        finish();
    }

    private void enableEditing() {
        nameEditText.setEnabled(true);
        phoneEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        bloodGroup.setEnabled(true);
        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        nameEditText.setEnabled(false);
        phoneEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        bloodGroup.setEnabled(false);
        saveBtn.setVisibility(View.GONE);
        editBtn.setVisibility(View.VISIBLE);
    }
    private void updateUserData(String name, String phone) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if (currentUser != null) {
            // Use the UID of the current user to update the corresponding document in the "Patients" collection
            db.collection("patients")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            documentSnapshot.getReference().update("name", name, "phone_number", phone)
                                    .addOnSuccessListener(aVoid -> {
                                        // Data updated successfully
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle error
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                    });
        }
    }

}