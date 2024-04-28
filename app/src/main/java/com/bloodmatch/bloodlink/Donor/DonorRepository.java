package com.bloodmatch.bloodlink.Donor;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonorRepository {
    private FirebaseFirestore firestore;
    private CollectionReference donorRepository;

    public DonorRepository() {
        firestore = FirebaseFirestore.getInstance();
        donorRepository = firestore.collection("donors");
    }

    public void getDonorById(String donorID, final OnPatientClickListner listener) {
        donorRepository.document(donorID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Donor donor = document.toObject(Donor.class);
                                if (donor != null) {
                                    listener.onSuccess(donor);
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
        void onSuccess(Donor donor);
        void onError(String errorMessage);
    }
}
