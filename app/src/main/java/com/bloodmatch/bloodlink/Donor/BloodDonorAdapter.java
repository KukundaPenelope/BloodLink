package com.bloodmatch.bloodlink.Donor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;

import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {
    private List<Donor> donorList;
    private Context context;

    private static final int PERMISSION_REQUEST_CODE = 1;

    public BloodDonorAdapter(List<Donor> donorList) {
        this.donorList = donorList;
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
        Donor donor = donorList.get(position);

        // Set the donor information in the item layout
        holder.nameTextView.setText(donor.getFirstName() + " " + donor.getLastName());
        holder.districtTextView.setText(donor.getLocation());
        holder.contactTextView.setText(donor.getPhoneNumber());
        holder.bloodGroupText.setText(donor.getBloodGroup());

        // Set click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDonorPopup(donor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, districtTextView, contactTextView, bloodGroupText;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            districtTextView = itemView.findViewById(R.id.districtTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
            bloodGroupText = itemView.findViewById(R.id.bloodGroupText);
        }
    }

    private void showDonorPopup(final Donor donor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.donor_dialog);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView nameTextView = dialog.findViewById(R.id.nameTextView);
        TextView districtTextView = dialog.findViewById(R.id.districtTextView);
        TextView contactTextView = dialog.findViewById(R.id.contactTextView);
        LinearLayout callLayout = dialog.findViewById(R.id.callLayout);
        LinearLayout locateLayout = dialog.findViewById(R.id.locateLayout);

        nameTextView.setText(donor.getFirstName() + " " + donor.getLastName());
        districtTextView.setText(donor.getLocation());
        contactTextView.setText(donor.getPhoneNumber());

        final Donor finalDonor = donor; // Create a final variable to capture the donor object

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the donor
                String phoneNumber = finalDonor.getPhoneNumber();
                String countryCode="+256";
                String phoneNumberWithCountryCode = countryCode + phoneNumber;
                if (!phoneNumber.isEmpty()) {
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
                String address = finalDonor.getLocation();
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
}