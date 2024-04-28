package com.bloodmatch.bloodlink.Donor;

import static android.text.format.DateUtils.formatDateTime;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.Patient.Request;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocateBloodDonors extends AppCompatActivity implements BloodDonorAdapter.OnRequestClickListener {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private BloodDonorAdapter donorAdapter;
    private List<Donor> donorList;
    private Toolbar backTool;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference requestsCollection = db.collection("requests");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_blood_donors);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Initialize UI elements
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);
        backTool = findViewById(R.id.toolbar);
        setSupportActionBar(backTool);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        donorList = new ArrayList<>();

        donorAdapter = new BloodDonorAdapter(donorList, this);
        recyclerView.setAdapter(donorAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchedBloodGroup = s.toString().trim();
                filterBloodDonors(searchedBloodGroup);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fetchAllBloodDonors();
    }

    private void filterBloodDonors(String bloodGroup) {
        if (TextUtils.isEmpty(bloodGroup)) {
            fetchAllBloodDonors();
        } else {
//            db.collection("hospital").document().getId();
            Query query = db.collection("donors").whereEqualTo("blood_type", bloodGroup);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    donorList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Donor donor = document.toObject(Donor.class);
                        String doc=document.getId();
                        donor.setBlood_type(document.getString("blood_type"));
                        donor.setPhone_number(document.getString("phone_number"));
                        donor.setHospital_id(document.getString("hospital_id"));
//            
                        donorList.add(donor);
                    }
                    donorAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LocateBloodDonors.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchAllBloodDonors() {
        db.collection("donors").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donorList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donor donor = document.toObject(Donor.class);
                            donor.setBlood_type(document.getString("blood_type"));
                            donor.setPhone_number(document.getString("phone_number"));
                            donor.setHospital_id(document.getString("hospital_id"));

                            donorList.add(donor);
                        }
                        donorAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LocateBloodDonors.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //public  void getdonorId(String id){
//// Get a reference to the "donors" collection
//    CollectionReference donorsCollectionRef = db.collection("donors");
//
//// Query for documents in the "donors" collection
//    donorsCollectionRef.limit(1)  // Limit the query to retrieve only one document
//            .get()
//            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if (documentSnapshot != null && documentSnapshot.exists()) {
//                        String donorUserId = documentSnapshot.getId();
//                        // Use donorUserId as needed
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.w(TAG, "Error getting documents.", e);
//                }
//            });
//
//return;
//}
    public void onRequestClick(Donor donor) {
        String user_id = currentUser.getUid();
        db.collection("patients").whereEqualTo("user_id", user_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot patientSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String patientId = patientSnapshot.getString("patient_id");

                        // Proceed with sending request using patientId
                        FirebaseMessaging.getInstance().getToken()
                                .addOnSuccessListener(token -> {
                                    // Token retrieved successfully
                                    String requestTime = formatDateTime(Calendar.getInstance().getTime()); // Format current datetime as requestTime

                                    // Create a new Request object
                                    Request request = new Request();
                                    request.setDonor_id(donor.getDonor_id()); // Assuming donor_id is the field that uniquely identifies a donor
                                    request.setPatient_id(patientId);
                                    request.setRequest_time(requestTime);
                                    request.setStatus("pending");
                                    request.setFcmToken(token);
                                    String requestID=requestsCollection.document().getId();
                                    request.setRequest_id(requestID);
                                    // Save the request to the "Requests" collection
                                    requestsCollection.document(requestID).set(request)
                                            .addOnSuccessListener(documentReference -> {
                                                Toast.makeText(this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                                request.setUpdated(formatDateTime(Calendar.getInstance().getTime()));
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error occurred while sending request
                                                Toast.makeText(this, "Error sending request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Error occurred while fetching token
                                    Toast.makeText(this, "Error fetching token: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Patient document not found for the user
                        Toast.makeText(this, "Patient data not found for the current user", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred while fetching patient data
                    Toast.makeText(this, "Error fetching patient data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }








    private String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.getDefault());
        return sdf.format(date);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
