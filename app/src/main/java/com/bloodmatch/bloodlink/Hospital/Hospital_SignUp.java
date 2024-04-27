package com.bloodmatch.bloodlink.Hospital;

import static com.bloodmatch.bloodlink.BloodBank.BloodBankAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Hospital_SignUp extends AppCompatActivity {

    private static final String TAG = "Hospital_SignUp";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference hospitalCollection = db.collection("hospital");
    private CollectionReference usersCollection = db.collection("users");
    private Spinner bloodBankSpinner;
    private FirebaseAuth firebaseAuth;
    private EditText hosptalNameEditText;
    private EditText districtEditText;
    private EditText emailEditText;
    private EditText phoneholder;
    private EditText locationEditText;
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_sign_up);

        hosptalNameEditText = findViewById(R.id.hospitalEditText);
        districtEditText = findViewById(R.id.districtEditText);
        districtEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(com.bloodmatch.bloodlink.Hospital.Hospital_SignUp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(com.bloodmatch.bloodlink.Hospital.Hospital_SignUp.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                } else {
                    fetchLocation();
                }
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

//        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
//        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        genderSpinner.setAdapter(adapter3);
//        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.bloodg_array, android.R.layout.simple_spinner_item);
//        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        bloodGroupSpinner.setAdapter(adapter4);

        Button signUpButton = findViewById(R.id.sign_upb);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
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
                    Geocoder geocoder = new Geocoder(com.bloodmatch.bloodlink.Hospital.Hospital_SignUp.this, Locale.getDefault());
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
    private void signUp() {
        String hospitalName = hosptalNameEditText.getText().toString().trim();
        String districtName = districtEditText.getText().toString().trim();

        if (hospitalName.isEmpty() || districtName.isEmpty()) {
            Toast.makeText(Hospital_SignUp.this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show dialog for additional patient info
        showPopupForm(hospitalName, districtName);
    }

    private void showPopupForm(String hospitalName, String district) {
        View dialogView = getLayoutInflater().inflate(R.layout.hospital_pop_up, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        phoneholder = dialogView.findViewById(R.id.phoneholder);
        emailEditText = dialogView.findViewById(R.id.emailEditText);

        passwordEditText = dialogView.findViewById(R.id.passwordEditText);

        Button submitButton = dialogView.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneholder.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Hospital_SignUp.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    signUpHospital(email, phoneNumber, password, hospitalName);
                    dialog.dismiss();
                }
            }
        });
    }

    private void fetchBloodBanks() {
        CollectionReference hospitalsCollection = db.collection("blood_bank");
        hospitalsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> hospitalNames = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String bloodBankName = documentSnapshot.getString("name");
                    // Check if the bloodbank name is not already in the list
                    if (!hospitalNames.contains(bloodBankName)) {
                        hospitalNames.add(bloodBankName);
                    }
                }
                populateHospitalSpinner(hospitalNames);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error fetching bloodbanks: " + e.getMessage());
            }
        });
    }

    private void populateHospitalSpinner(List<String> hospitalNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hospitalNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodBankSpinner.setAdapter(adapter);
    }

    private void signUpHospital(String email, String phoneNumber, String password, String hospitalName) {
        // Create a user with email and password authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // User creation successful, proceed with saving hospital data
                            String userId = firebaseAuth.getCurrentUser().getUid(); // Get the newly created user's ID
                            Map<String, Object> hospitalData = new HashMap<>();
                            String hospitalID = hospitalCollection.document().getId();
                            hospitalData.put("name", hospitalName);
                            hospitalData.put("email", email);
                            hospitalData.put("phone_number", phoneNumber);
                            hospitalData.put("role", "hospital");
                            hospitalData.put("user_id", userId);
                            hospitalData.put("created", getCurrentDateTime());
                            hospitalData.put("created_by", email);
                            hospitalData.put("hospital_id",hospitalID);

                            hospitalCollection.document(hospitalID)
                                    .set(hospitalData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Hospital data added to Firestore successfully");

                                            // Save user data to "users" collection with same auto-generated document ID
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("hospital_id", hospitalID);
                                            userData.put("email", email);
                                            userData.put("role", "hospital");
                                            userData.put("created", getCurrentDateTime());
                                            userData.put("created_by", email);
                                            userData.put("user_id", userId);
                                            userData.get("user_id");

                                            usersCollection.document(userId).set(userData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "User data added to Firestore successfully");

                                                            // Redirect to main activity or any other desired activity
                                                            startActivity(new Intent(Hospital_SignUp.this, MainActivity3.class));
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e(TAG, "Error adding user data to Firestore", e);
                                                            Toast.makeText(Hospital_SignUp.this, "Error creating account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error adding hospital data to Firestore", e);
                                            Toast.makeText(Hospital_SignUp.this, "Error creating account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // User creation failed
                            Log.e(TAG, "Error creating user: " + task.getException().getMessage());
                            Toast.makeText(Hospital_SignUp.this, "Error creating user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Define the getCurrentDateTime method in your class
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        return sdf.format(new Date());
    }

}

