package com.bloodmatch.bloodlink.Donor;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bloodmatch.bloodlink.MainActivity;
import com.bloodmatch.bloodlink.MainActivity2;
import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.Patient.Patient_SignUp;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Donor_SignUp extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText ageEditText;
    private Spinner genderEditText,bloodG;
    private EditText emailEditText;
    private EditText phoneholder;
    private EditText dobEditText;
    private EditText locationEditText;
    private EditText passwordEditText;

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Donors");

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        bloodG=findViewById(R.id.bloodGroupEditText);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderEditText.setAdapter(adapter3);
        ArrayAdapter<CharSequence> adapter4= ArrayAdapter.createFromResource(this, R.array.bloodg_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodG.setAdapter(adapter4);
        Button signUpButton = findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {





            @Override

            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String age = ageEditText.getText().toString().trim();
                String gender = genderEditText.getSelectedItem().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(Donor_SignUp.this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    showPopupForm();
                }
            }
        });
    }

    private void showPopupForm() {
        View dialogView = getLayoutInflater().inflate(R.layout.pop_up, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        emailEditText = dialogView.findViewById(R.id.emailEditText);
        phoneholder = dialogView.findViewById(R.id.phoneholder);
        dobEditText = dialogView.findViewById(R.id.dobEditText);
        locationEditText = dialogView.findViewById(R.id.locationEditText);
        passwordEditText = dialogView.findViewById(R.id.passwordEditText);


        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Donor_SignUp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Donor_SignUp.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                } else {
                    fetchLocation();
                }
            }
        });

        Button submitButton = dialogView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneholder.getText().toString().trim();
                String dob = dobEditText.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || phoneNumber.isEmpty() || dob.isEmpty() || location.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Donor_SignUp.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveToFirebase(email, phoneNumber, dob, location, password);
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(Donor_SignUp.this, MainActivity3.class));
                }
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Donor_SignUp.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dobEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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
                    Geocoder geocoder = new Geocoder(Donor_SignUp.this, Locale.getDefault());
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

    private void saveToFirebase(String email, String phoneNumber, String dob, String location, String password) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String gender = genderEditText.getSelectedItem().toString();
        String bloodGroup=bloodG.getSelectedItem().toString().trim();
        String Email= emailEditText.getText().toString().trim();
        String psd=passwordEditText.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(Email, psd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String donorId = firebaseAuth.getCurrentUser().getUid();
                            //store data
                            Donor donor = new Donor(donorId,firstName, lastName, age, gender, bloodGroup, email, phoneNumber, dob, location, password);
                            databaseReference.child(donorId).setValue(donor);
                            Toast.makeText(Donor_SignUp.this, "Account Created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Donor_SignUp.this, MainActivity3.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Donor_SignUp.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}