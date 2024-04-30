package com.bloodmatch.bloodlink.Donor;

import androidx.annotation.NonNull;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class PatientRepository {
    private FirebaseFirestore firestore;
    private CollectionReference patientRepository;

    public PatientRepository() {
        firestore = FirebaseFirestore.getInstance();
        patientRepository= firestore.collection("patients");

    }

    public void getPatientById(String patientID, final OnPatientClickListner listener) {
        patientRepository.document(patientID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Patient patient = document.toObject(Patient.class);
                                if (patient != null) {
                                    listener.onSuccess(patient);
                                } else {
                                    listener.onError("Donor data is null");
                                }
                            } else {
                                listener.onError("Donor document not found");
                            }
                        } else {
                            listener.onError("Failed to fetch Donor data: " + task.getException().getMessage());
                        }
                    }
                });
    }
    public interface OnPatientClickListner {
        void onSuccess(Patient patient);
        void onError(String errorMessage);
    }
}
