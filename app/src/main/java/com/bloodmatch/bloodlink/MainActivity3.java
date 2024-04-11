package com.bloodmatch.bloodlink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodmatch.bloodlink.BloodBank.BloodBank_Navigation;
import com.bloodmatch.bloodlink.Donor.Donor_Navigation;
import com.bloodmatch.bloodlink.Hospital.Hospital_Navigation;
import com.bloodmatch.bloodlink.Patient.Patient_Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {
    AppCompatButton signin;

    TextView signup,forgot_paswd;
    EditText email, password;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        signin=findViewById(R.id.sign);
        signup=findViewById(R.id.reg);
        forgot_paswd=findViewById(R.id.forgotpsd);
        signin = findViewById(R.id.sign);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pswd);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity3.this, Donor_Navigation.class);
//                startActivity(intent);
//            }
//        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {
                    Toast.makeText(MainActivity3.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkUserTypeAndRedirect();
//                                    saveUserTypeAndRedirect("Donors", Donor_Navigation.class);
                                } else {
                                    Toast.makeText(MainActivity3.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity3.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity3.this, SignUP.class);
                startActivity(intent);
            }

        });

        forgot_paswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity3.this, ForgotPassword.class));
            }
        });

    }

    private void checkUserTypeAndRedirect() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Check under Donors
            DatabaseReference donorsRef = databaseReference.child("Donors").child(uid);
            donorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User is a donor
                        saveUserTypeAndRedirect("Donors", Donor_Navigation.class);
                        return;
                    }

                    // If not a donor, check under Patients
                    DatabaseReference patientsRef = databaseReference.child("Patients").child(uid);
                    patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // User is a patient
                                saveUserTypeAndRedirect("Patients", Patient_Navigation.class);
                                return;
                            }

                            // If not a patient, check under Hospitals
                            DatabaseReference hospitalsRef = databaseReference.child("Hospitals").child(uid);
                            hospitalsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // User is a hospital
                                        saveUserTypeAndRedirect("Hospitals", Hospital_Navigation.class);
                                        return;
                                    }

                                    // If not a hospital, check under BloodBanks
                                    DatabaseReference bloodBanksRef = databaseReference.child("bloodbanks").child(uid);
                                    bloodBanksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // User is a blood bank
                                                saveUserTypeAndRedirect("bloodbanks", BloodBank_Navigation.class);
                                                return;
                                            }

                                            // Unknown user type
                                            Toast.makeText(MainActivity3.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(MainActivity3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(MainActivity3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Utility method to save user type to SharedPreferences, start an activity, and finish the current one
    private void saveUserTypeAndRedirect(String userType, Class<?> targetActivity) {
        SharedPreferences sharedPreferences = getSharedPreferences("BloodSync", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userType", userType);
        editor.apply();

        Intent intent = new Intent(MainActivity3.this, targetActivity);
        startActivity(intent);
        finish();
    }

    }
