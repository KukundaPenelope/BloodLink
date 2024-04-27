package com.bloodmatch.bloodlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bloodmatch.bloodlink.BloodBank.BloodBank_Navigation;
import com.bloodmatch.bloodlink.Donor.Donor_Navigation;
import com.bloodmatch.bloodlink.Hospital.Hospital_Navigation;
import com.bloodmatch.bloodlink.Patient.Patient_Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class MainActivity3 extends AppCompatActivity {
    AppCompatButton signin;

    TextView signup,forgot_paswd;
    EditText email, password;

    FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;
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
// Initialize Google sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Create a Google sign-in client
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set click listener for the Google sign-in button
        ImageView googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });


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

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Query Firestore for the document corresponding to the user's ID
            db.collection("users").whereEqualTo("user_id", uid).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String userType = documentSnapshot.getString("role");

                            if (userType != null) {
                                // Redirect based on the user's role
                                switch (userType) {
                                    case "donor":
                                        saveUserTypeAndRedirect("donor", Donor_Navigation.class);
                                        break;
                                    case "patient":
                                        saveUserTypeAndRedirect("patient", Patient_Navigation.class);
                                        break;
                                    case "hospital":
                                        saveUserTypeAndRedirect("hospital", Hospital_Navigation.class);
                                        break;
                                    default:
                                        Toast.makeText(MainActivity3.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } else {
                                Toast.makeText(MainActivity3.this, "User type not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity3.this, "User document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity3.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the Google sign-in result
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google sign-in was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google sign-in failed, update UI accordingly
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Google sign-in authentication successful
                            checkUserTypeAndRedirect();
                        } else {
                            // Google sign-in authentication failed
                            Toast.makeText(MainActivity3.this, "Google sign-in authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }
