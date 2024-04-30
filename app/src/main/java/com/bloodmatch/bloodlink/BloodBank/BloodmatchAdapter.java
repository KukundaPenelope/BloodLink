package com.bloodmatch.bloodlink.BloodBank;

import static com.bloodmatch.bloodlink.BloodBank.BloodBankAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.R;

import java.util.List;
import java.util.Map;

public class BloodmatchAdapter extends RecyclerView.Adapter<BloodmatchAdapter.BloodmatchViewHolder> {
    private List<BloodBanks> bloodBanks;
    private Context context;

    public BloodmatchAdapter(Context context, List<BloodBanks> bloodBanks) {
        this.context = context;
        this.bloodBanks = bloodBanks;
    }

    @NonNull
    @Override
    public BloodmatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bloodmatch, parent, false);
        return new BloodmatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodmatchViewHolder holder, int position) {
        final BloodBanks bloodBank = bloodBanks.get(position);
        holder.bind(bloodBank);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new dialog
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.blood_banj_dialog, null);
                dialogBuilder.setView(dialogView);

                // Set the blood bank details in the dialog's views
                TextView nameTextView = dialogView.findViewById(R.id.nameTextView);
                TextView districtTextView = dialogView.findViewById(R.id.districtTextView);
                TextView addressTextView = dialogView.findViewById(R.id.addressTextView);
                TextView contactTextView = dialogView.findViewById(R.id.contactTextView);

                nameTextView.setText(bloodBank.getName());
                districtTextView.setText(bloodBank.getDistrict());
                addressTextView.setText(bloodBank.getAddress());
                contactTextView.setText(bloodBank.getContact());

                // Find the locate_Layout TextView in the dialog view
                LinearLayout locate_Layout = dialogView.findViewById(R.id.locateLayout);

                LinearLayout call_Layout = dialogView.findViewById(R.id.callLayout);
                // Set the blood bank details in the item view
                call_Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneNumber = bloodBank.getContact();
                        String countryCode="+256";
                        String phoneNumberWithCountryCode = countryCode + phoneNumber;
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumberWithCountryCode));
                        // Check phone call permissions
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            // Phone call permissions granted, start the dialer intent
                            context.startActivity(intent);
                        } else {
                            // Phone call permissions not granted, request the permissions
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
                        }
                    }
                });
                // Set the click listener for the locate_Layout TextView in the dialog
                locate_Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String address = bloodBank.getAddress();
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

                // Show the dialog
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return bloodBanks.size();
    }

    public static class BloodmatchViewHolder extends RecyclerView.ViewHolder {
        private TextView bloodTypeTextView;
        private TextView amountTextView;
        private TextView nameTextView;
        private TextView locationTextView;

        public BloodmatchViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodTypeTextView = itemView.findViewById(R.id.bloodtype);
            amountTextView = itemView.findViewById(R.id.amount);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.addressTextView);
        }

        public void bind(BloodBanks bloodBank) {
            // Get blood type and its amount
            Map<String, Long> bloodAmounts = bloodBank.getBloodAmounts();
            StringBuilder bloodTypes = new StringBuilder();
            StringBuilder amounts = new StringBuilder();
            for (Map.Entry<String, Long> entry : bloodAmounts.entrySet()) {
                bloodTypes.append(entry.getKey()).append("\n");
                amounts.append(entry.getValue()).append("\n");
            }

            // Set text views
            bloodTypeTextView.setText(bloodTypes.toString());
            amountTextView.setText(amounts.toString());
            nameTextView.setText(bloodBank.getName());
            locationTextView.setText(bloodBank.getAddress());
        }
    }
}
