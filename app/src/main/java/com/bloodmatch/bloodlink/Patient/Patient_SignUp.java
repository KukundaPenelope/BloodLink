package com.bloodmatch.bloodlink.Patient;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Patient_SignUp extends AppCompatActivity {

    private static final String TAG = "Patient_SignUp";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patientsCollection = db.collection("patients");
    private CollectionReference usersCollection = db.collection("users");
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText ageEditText;
    private Spinner genderSpinner;
    private Spinner bloodGroupSpinner;
    private Spinner hospitalSpinner;
    private FirebaseAuth firebaseAuth;
    private EditText dobEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        bloodGroupSpinner = findViewById(R.id.bloodGroupEditText);
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodg_array, android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);


        Button signUpButton = findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty()) {
            Toast.makeText(Patient_SignUp.this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show dialog for additional patient info
        showPopupForm(firstName, lastName, age, gender, bloodGroup);
    }

    private void showPopupForm(String firstName, String lastName, String age, String gender, String bloodGroup) {
        View dialogView = getLayoutInflater().inflate(R.layout.pop_up, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
        EditText phoneEditText = dialogView.findViewById(R.id.phoneholder);
         dobEditText = dialogView.findViewById(R.id.dobEditText);
        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
        Button submitButton = dialogView.findViewById(R.id.submitButton);
        hospitalSpinner = dialog.findViewById(R.id.hospitalSpinner);

        fetchHospitals();
        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneEditText.getText().toString().trim();
                String dob = dobEditText.getText().toString().trim();
                String hospital = hospitalSpinner.getSelectedItem().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || phoneNumber.isEmpty() || dob.isEmpty() || hospital.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Patient_SignUp.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    signUpPatient(firstName, lastName, age, gender, bloodGroup, email, phoneNumber, dob, hospital, password);
                    dialog.dismiss();
                }
            }
        });
    }

    private void fetchHospitals() {
        CollectionReference hospitalsCollection = db.collection("hospital");
        hospitalsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> hospitalNames = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String hospitalName = documentSnapshot.getString("name");
                    // Check if the hospital name is not already in the list
                    if (!hospitalNames.contains(hospitalName)) {
                        hospitalNames.add(hospitalName);
                    }
                }
                populateHospitalSpinner(hospitalNames);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error fetching hospitals: " + e.getMessage());
            }
        });
    }

    private void populateHospitalSpinner(List<String> hospitalNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hospitalNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(adapter);
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Patient_SignUp.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dobEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void signUpPatient(String firstName, String lastName, String age, String gender, String bloodGroup, String email, String phoneNumber, String dob, String hospitalName, String password) {
        // Create a user with email and password authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User creation successful, proceed with saving patient data
                            String userId = firebaseAuth.getCurrentUser().getUid(); // Get the newly created user's ID
                            db.collection("hospital")
                                    .whereEqualTo("name", hospitalName)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                DocumentSnapshot hospitalSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                String hospitalId = hospitalSnapshot.getString("hospital_id");
                                                Map<String, Object> patientData = new HashMap<>();
                                                String patientId = patientsCollection.document().getId();
                                                patientData.put("hospital_id", hospitalId);
                                                patientData.put("blood_type", bloodGroup);
                                                String createdDate = getCurrentDateTime();
                                                patientData.put("created", createdDate);
                                                patientData.put("created_by", email);
                                                patientData.put("email", email);
                                                patientData.put("gender", gender);
                                                patientData.put("name", firstName + " " + lastName);
                                                patientData.put("age", age);
                                                patientData.put("phone_number", phoneNumber);
                                                patientData.put("dob", dob);
                                                patientData.put("role", "patient");
                                                patientData.put("user_id",userId);
                                                patientData.put("patient_id", patientId);
                                                patientsCollection.document(patientId).set(patientData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "Patient data added to Firestore successfully");
                                                                // Save user data to "users" collection with same auto-generated document ID
                                                                Map<String, Object> userData = new HashMap<>();
                                                                userData.put("hospital_id", hospitalId); // Assuming not available for patients
                                                                userData.put("blood_type", bloodGroup);
                                                                String createdDate = getCurrentDateTime();
                                                                userData.put("created", createdDate);
                                                                userData.put("email", email);
                                                                userData.put("name", firstName + " " + lastName);
                                                                userData.put("role", "patient");
                                                                userData.put("created_by", email);
                                                                userData.put("user_id",userId);

                                                                usersCollection.document(userId).set(userData)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d(TAG, "User data added to Firestore successfully");

                                                                                // Redirect to main activity or any other desired activity
                                                                                startActivity(new Intent(Patient_SignUp.this, MainActivity3.class));
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.e(TAG, "Error adding user data to Firestore", e);
                                                                                Toast.makeText(Patient_SignUp.this, "Error creating account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "Error adding patient data to Firestore", e);
                                                                Toast.makeText(Patient_SignUp.this, "Error creating account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                // Hospital not found
                                                Log.e(TAG, "Hospital not found in Firestore");
                                                Toast.makeText(Patient_SignUp.this, "Hospital not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error querying hospitals: " + e.getMessage());
                                            Toast.makeText(Patient_SignUp.this, "Error querying hospitals: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // User creation failed
                            Log.e(TAG, "Error creating user: " + task.getException().getMessage());
                            Toast.makeText(Patient_SignUp.this, "Error creating user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

