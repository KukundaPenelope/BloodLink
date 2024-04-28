package com.bloodmatch.bloodlink.Donor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.BloodBank.Patient_Locator;
import com.bloodmatch.bloodlink.Patient.New_Notifications;
import com.bloodmatch.bloodlink.Patient.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// Import statements...

public class ApprovalDialogFragment extends DialogFragment {

    private Request request;
    private int position;

    public ApprovalDialogFragment(Request request, int position) {
        this.request = request;
        this.position = position;
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Action")
                .setMessage("Approve or cancel this request?")
                .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        approveRequest();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelRequest();
                    }
                });

        return builder.create();
    }

    private void approveRequest() {
        // Update the status of the request to "approved" in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference requestRef = db.collection("requests").document(request.getRequest_id());
        requestRef.update("status", "approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Remove the approved request from the "New" fragment's dataset
                            Fragment parentFragment = getParentFragment();
                            if (parentFragment instanceof New_Notifications) {
                                ((New_Notifications) parentFragment).removeRequest(request);
                            }

                            // Fetch the patient's location from Firestore
                            fetchPatientLocation(request.getPatient_id());
                        } else {
                            // Handle error
                        }
                    }
                });
    }


    private void cancelRequest() {
        // Update the status of the request to "cancelled" in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference requestRef = db.collection("requests").document(request.getRequest_id());
        requestRef.update("status", "rejected")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Remove the cancelled request from the "New" fragment's dataset
                            Fragment parentFragment = getParentFragment();
                            if (parentFragment instanceof New_Notifications) {
                                ((New_Notifications) parentFragment).removeRequest(request);
                            }
                        } else {
                            // Handle error
                        }
                    }
                });
    }

    private void storePatientLocation(String patientLocation) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Assuming "patients" is the collection storing patient data, and each document has a field called "location"
        DocumentReference patientRef = db.collection("patients").document(request.getPatient_id());
        patientRef.update("location", patientLocation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("Patient document does not exist");

                        } else {
                            // Handle error
                        }
                    }
                });
    }
    private void fetchPatientLocation(String patientId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference patientRef = db.collection("patients").document(patientId);
        patientRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String patientLocation = documentSnapshot.getString("location");
                            if (patientLocation != null) {
                                // Store the patient's location in Firestore
                                storePatientLocation(patientLocation);
                                // Start the Maps activity and pass the necessary data
                                Intent intent = new Intent(getActivity(), Patient_Locator.class);
                                startActivity(intent);
                            }else {
                            // Handle the case where the patient's location is not available
                            showToast("Patient location not available");
                        }
                    } else {
                        // Handle the case where the patient document does not exist
                        showToast("Patient document does not exist");
                    }
                }
    })
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // Handle failure to fetch patient's location
            showToast("Failed to fetch patient's location");
        }
    });
    }
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
