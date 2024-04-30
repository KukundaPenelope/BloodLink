package com.bloodmatch.bloodlink.Donor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.Patient.New_Notifications;
import com.bloodmatch.bloodlink.Patient.Request;
import com.google.firebase.firestore.FirebaseFirestore;

public class ApprovalDialogFragment extends DialogFragment {

    private Request request;
    private int position;
    private Context context;

    public ApprovalDialogFragment(Request request) {
        this.request = request;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("requests").document(request.getRequest_id())
                .update("status", "approved")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Fragment parentFragment = getParentFragment();
                        if (parentFragment instanceof New_Notifications) {
                            ((New_Notifications) parentFragment).removeRequest(request);
                            fetchPatientLocation(request.getPatient_id());
                        }
                    } else {
                        Toast.makeText(context, "Failed to approve request", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cancelRequest() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("requests").document(request.getRequest_id())
                .update("status", "rejected")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Fragment parentFragment = getParentFragment();
                        if (parentFragment instanceof New_Notifications) {
                            ((New_Notifications) parentFragment).removeRequest(request);
                        }
                    } else {
                        Toast.makeText(context, "Failed to cancel request", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPatientLocation(String patientId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("patients").document(patientId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String patientLocation = task.getResult().getString("location");
                        if (patientLocation != null && !patientLocation.isEmpty()) {
                            launchMapIntent(patientLocation);
                        } else {
                            Toast.makeText(context, "Patient location not available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Failed to fetch patient location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void launchMapIntent(String location) {
        Uri locationUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
