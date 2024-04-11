package com.bloodmatch.bloodlink.Donor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.Patient.New_Notifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bloodmatch.bloodlink.Patient.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ApprovalDialogFragment extends DialogFragment {

    private Request request;
    private int position;
    private Fragment parentFragment;

    public ApprovalDialogFragment(Request request, int position) {
        this.request = request;
        this.position = position;
        this.parentFragment = parentFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof New_Notifications) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Action")
                    .setMessage("Approve or cancel this request?")
                    .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update the status of the request to "approved" in Firebase Realtime Database
                            DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Requests").child(request.getRequestId());
                            requestRef.child("requestStatus").setValue("Approved");

                            // Remove the approved request from the "New" fragment's dataset
                            ((New_Notifications) parentFragment).removeRequest(request);

                            // Add the approved request to the "Approved" fragment's dataset
                            ((ApprovedNotificationsFragment) requireParentFragment()).addRequest(request);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update the status of the request to "cancelled" in Firebase Realtime Database
                            DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Requests").child(request.getRequestId());
                            requestRef.child("requestStatus").setValue("Cancelled");

                            // Remove the cancelled request from the "New" fragment's dataset
                            ((New_Notifications) parentFragment).removeRequest(request);

                            // Add the cancelled request to the "Cancelled" fragment's dataset
                            ((CancelledNotificationsFragment) requireParentFragment()).addRequest(request);
                        }
                    });

            return builder.create();
        } else {
            // Return an empty dialog if not in the New_Notifications fragment
            return new AlertDialog.Builder(getActivity()).create();
        }
    }


}
