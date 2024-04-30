package com.bloodmatch.bloodlink.DonationSites;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
import com.bloodmatch.bloodlink.Donor.Donor;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String patientBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get the patient's blood group from the intent or a database
        patientBloodGroup = getIntent().getStringExtra("blood_type");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        mMap.setMyLocationEnabled(true);

        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                            // Retrieve nearby blood banks and donors with the same blood group as the patient
                            try {
                                retrieveNearbyBloodBanksAndDonors(currentLocation);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Log.w("MapsActivity", "getLastLocation:exception", task.getException());
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void retrieveNearbyBloodBanksAndDonors(LatLng currentLocation) throws IOException {
        // Implement logic to retrieve nearby blood banks and donors with the same blood group as the patient
        // Use a database or API to retrieve the data
        // ...

        // Add markers to the map for the nearby blood banks and donors
        List<BloodBanks> bloodBanks = getBloodBanks(); // Implement logic to retrieve blood banks
        for (BloodBanks bloodBank : bloodBanks) {
            String bloodBankAddress = bloodBank.getAddress();
            LatLng bloodBankLocation = getLatLngFromAddress(bloodBankAddress);
            if (bloodBankLocation != null) {
                mMap.addMarker(new MarkerOptions().position(bloodBankLocation).title(bloodBank.getName()));
            }
        }

        List<Donor> donors = getDonors(); // Implement logic to retrieve donors
        for (Donor donor : donors) {
            String donorAddress = donor.getLocation();
            LatLng donorLocation = getLatLngFromAddress(donorAddress);
            if (donorLocation != null) {
                mMap.addMarker(new MarkerOptions().position(donorLocation).title(donor.getName()));
            }
        }
    }

    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e("MapsActivity", "Geocoder IOException: " + e.getMessage());
            return null; // Return null or handle the exception according to your app's requirements
        } catch (IllegalArgumentException e) {
            Log.e("MapsActivity", "Geocoder IllegalArgumentException: " + e.getMessage());
            return null; // Return null or handle the exception according to your app's requirements
        }
    }

    private List<BloodBanks> getBloodBanks() {
        // Implement logic to retrieve blood banks from the database or API
        // ...

        // Example data
        List<BloodBanks> bloodBanks = new ArrayList<>();
        BloodBanks bloodBank = new BloodBanks();
        bloodBank.getName();
        bloodBank.getAddress();

        bloodBanks.add(bloodBank);

        return bloodBanks;
    }

    private List<Donor> getDonors() {

        List<Donor> donors = new ArrayList<>();
        Donor donor = new Donor();
        donor.getName();
        donor.getLocation();
        donors.add(donor);

        return donors;
    }
}