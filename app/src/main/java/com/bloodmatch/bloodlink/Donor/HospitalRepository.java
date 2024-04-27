package com.bloodmatch.bloodlink.Donor;

import androidx.annotation.NonNull;

import com.bloodmatch.bloodlink.Hospital.Hospital;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HospitalRepository {
    private FirebaseFirestore firestore;
    private CollectionReference hospitalCollection;

    public HospitalRepository() {
        firestore = FirebaseFirestore.getInstance();
        hospitalCollection = firestore.collection("hospital");
    }

    public void getHospitalById(String hospitalId, final OnHospitalDataListener listener) {
        hospitalCollection.document(hospitalId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Hospital hospital = document.toObject(Hospital.class);
                                if (hospital != null) {
                                    listener.onSuccess(hospital);
                                } else {
                                    listener.onError("Hospital data is null");
                                }
                            } else {
                                listener.onError("Hospital document not found");
                            }
                        } else {
                            listener.onError("Failed to fetch hospital data: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public interface OnHospitalDataListener {
        void onSuccess(Hospital hospital);
        void onError(String errorMessage);
    }
}
