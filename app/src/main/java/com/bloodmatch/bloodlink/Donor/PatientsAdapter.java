package com.bloodmatch.bloodlink.Donor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {
    private List<Patient> patientList;
    private Context context;
    private PatientRepository patientRepository;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private OnRequestClickListener onRequestClickListener; // Listener variable

    // Constructor with listener parameter
    public PatientsAdapter(List<Patient> patientList, LocatePatients locatePatients) {
        this.patientList = patientList;
        this.patientRepository = new PatientRepository(); // Initialize the hospital repository
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_donors, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient patient = patientList.get(position);

        // Set the donor information in the item layout
        String PatientName = "Patient " + (position + 1);
        holder.nameTextView.setText(PatientName);
        holder.bloodGroupText.setText(patient.getBlood_type());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String donorId = patient.getPatient_id();
        PatientRepository repository = new PatientRepository();
        repository.getPatientById(donorId, new PatientRepository.OnPatientClickListner() {
            @Override
            public void onSuccess(Patient patient) {
                holder.districtTextView.setText(patient.getLocation());
                holder.contactTextView.setText(generatePseudoNumber());

            }

            @Override
            public void onError(String errorMessage) {
                holder.districtTextView.setText("Unknown District");
                holder.contactTextView.setText("Phone number not available");
            }


        });

        // Set click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDonorPopup(PatientName, patient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public interface OnRequestClickListener {
        void onRequestClick(Patient patient);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, districtTextView, contactTextView, bloodGroupText;

        public LinearLayout requestLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            districtTextView = itemView.findViewById(R.id.districtTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
            bloodGroupText = itemView.findViewById(R.id.bloodGroupText);
        }
    }

    private void showDonorPopup(final String donorName, Patient patient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.patient_dialog);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView nameTextView = dialog.findViewById(R.id.nameTextView2);
        TextView districtTextView = dialog.findViewById(R.id.districtTextView2);
        TextView contactTextView = dialog.findViewById(R.id.contactTextView2);
        LinearLayout callLayout = dialog.findViewById(R.id.callLayout2);
        LinearLayout locateLayout = dialog.findViewById(R.id.locateLayout2);

        nameTextView.setText(donorName);

        // Retrieve the donor information using the hospital ID
        patientRepository.getPatientById(patient.getPatient_id(), new PatientRepository.OnPatientClickListner() {
            @Override
            public void onSuccess(Patient patient) {
                districtTextView.setText(patient.getLocation());
                contactTextView.setText(generatePseudoNumber());
            }

            @Override
            public void onError(String errorMessage) {
                districtTextView.setText("Unknown District");
                contactTextView.setText("Phone number not available");
            }
        });


        final Patient finalPatient = patient;


        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = finalPatient.getPhone_number();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    String countryCode = "+256";
                    String phoneNumberWithCountryCode = countryCode + phoneNumber;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumberWithCountryCode));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        locateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = districtTextView.getText().toString();
                Uri locationUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);

                // Check location permissions
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Location permissions granted
                    // Check if location services are enabled
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        // Location services enabled, start the map intent
                        context.startActivity(mapIntent);
                    } else {
                        // Location services disabled, prompt the user to enable them
                        Toast.makeText(context, "Please enable location services", Toast.LENGTH_SHORT).show();
                        // Open the device's location settings screen
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(settingsIntent);
                    }
                } else {
                    // Location permissions not granted, request the permissions
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    // Method to generate pseudo number
    private String generatePseudoNumber() {
        // Generate a random 4-digit number
        Random random = new Random();
        int pseudoNumber = random.nextInt(9000) + 1000;
        return String.valueOf(pseudoNumber);
    }
}