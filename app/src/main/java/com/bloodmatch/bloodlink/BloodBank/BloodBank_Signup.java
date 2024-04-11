package com.bloodmatch.bloodlink.BloodBank;

import static com.bloodmatch.bloodlink.BloodBank.BloodBankAdapter.PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bloodmatch.bloodlink.Hospital.Hospial;
import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BloodBank_Signup extends AppCompatActivity {
    private EditText hosptalNameEditText;
    private EditText districtEditText;
    private EditText emailEditText;
    private EditText phoneholder;
    private EditText locationEditText;
    private EditText passwordEditText;

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("bloodbanks");

        hosptalNameEditText = findViewById(R.id.bloodbankEditText);
        districtEditText = findViewById(R.id.districtEditText);

        Button signUpButton = findViewById(R.id.sign_upb);
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                String name = hosptalNameEditText.getText().toString().trim();
                String district = districtEditText.getText().toString().trim();

                if (name.isEmpty() || district.isEmpty() ) {
                    Toast.makeText(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    showPopupForm();
                }
            }
        });
    }

    private void showPopupForm() {
        View dialogView = getLayoutInflater().inflate(R.layout.blood_bank_popup, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

//        locationEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
//                } else {
//                    fetchLocation();
//                }
//            }
//        });
        locationEditText = dialogView.findViewById(R.id.locationEditText);
        phoneholder = dialogView.findViewById(R.id.phoneholder);
        emailEditText = dialogView.findViewById(R.id.emailEditText);
        passwordEditText = dialogView.findViewById(R.id.passwordEditText);


        Button submitButton = dialogView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneholder.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || phoneNumber.isEmpty() || location.isEmpty() || password.isEmpty()) {
                    Toast.makeText(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveToFirebase(email, phoneNumber, location, password);
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, MainActivity3.class));
                }
            }
        });
    }


    private void fetchLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            String addressLine = addresses.get(0).getAddressLine(0);
                            locationEditText.setText(addressLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveToFirebase(String email, String phoneNumber, String location, String password) {
        String name = hosptalNameEditText.getText().toString().trim();
        String district = districtEditText.getText().toString().trim();
        String Email = emailEditText.getText().toString().trim();
        String Password = passwordEditText.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String hospitalId = firebaseAuth.getCurrentUser().getUid();
                            //store data
                            Hospial hospial = new Hospial(hospitalId, name, district, location, phoneNumber, email, password);
                            databaseReference.child(hospitalId).setValue(hospial);
                            Toast.makeText(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, "Account Created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, MainActivity3.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(com.bloodmatch.bloodlink.BloodBank.BloodBank_Signup.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
