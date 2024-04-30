package com.bloodmatch.bloodlink.BloodBank;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bloodmatch.bloodlink.Patient.Patient;
import com.bloodmatch.bloodlink.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Maps_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private FirebaseFirestore firestore;
    private LatLng userLocation;
    private List<Patient> patientsList;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        geocoder = new Geocoder(this);
        // Check permissions and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        } else {
            // Permissions already granted, proceed with location-related tasks
            initializeLocationCallback(); // Initialize location callback
            requestLocationUpdates(); // Request location updates
        }

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void addMarkersForNearbyBloodBanks(List<BloodBanks> nearbyBloodBanks) {
        for (BloodBanks bloodBank : nearbyBloodBanks) {
            LatLng location = getLocationFromAddress(geocoder, bloodBank.getAddress());
            if (location != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(bloodBank.getName())
                        .snippet(bloodBank.getAddress()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            }
        }
    }
    private void getNearbyBloodBanks(LatLng userLocation) {
        List<BloodBanks> nearbyBloodBanks = new ArrayList<>();

        // Retrieve the blood type of the patient
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;

        if (userId != null) {
            firestore.collection("patients").whereEqualTo("user_id",userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot donorSnapshot = queryDocumentSnapshots.getDocuments().get(0);
//                            String patient_Id = donorSnapshot.getString("patient_id");

                            String patientBloodType = donorSnapshot.getString("blood_type");

                        // Query blood banks collection in Firestore
                        firestore.collection("blood_bank")
                                .whereGreaterThan("blood_amount." + patientBloodType, 0)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        for (DocumentSnapshot document1 : task1.getResult()) {
                                            // Convert Firestore document to BloodBanks object
                                            BloodBanks bloodBank = document1.toObject(BloodBanks.class);

                                            // Convert address to LatLng
                                            LatLng bloodBankLocation = getLocationFromAddress(geocoder, document1.getString("address"));

                                            if (bloodBankLocation != null) {
                                                // Calculate distance between user location and blood bank location
                                                double distance = calculateDistance(userLocation, bloodBankLocation);

                                                // Check if blood bank is within 500 meters
                                                if (distance <= 500) {
                                                    // Check if blood bank has the same blood type as the patient
                                                    if (bloodBank.getBloodAmounts().containsKey(patientBloodType)) {
                                                        nearbyBloodBanks.add(bloodBank);
                                                    }
                                                }
                                            }
                                        }
                                        // Add markers for nearby blood banks on the map
                                        addMarkersForNearbyBloodBanks(nearbyBloodBanks);
                                    } else {
                                        Log.d("Maps_Activity", "Error getting blood banks: " + task1.getException());
                                    }
                                });
                    }
                });
            }
        }




    // Helper method to convert address to LatLng
    private LatLng getLocationFromAddress(Geocoder geocoder, String address) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to calculate distance between two locations
    private double calculateDistance(LatLng location1, LatLng location2) {
        double lat1 = location1.latitude;
        double lon1 = location1.longitude;
        double lat2 = location2.latitude;
        double lon2 = location2.longitude;

        double theta = lon1 - lon2;
        double distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));

        distance = Math.acos(distance);
        distance = Math.toDegrees(distance);
        distance = distance * 60 * 1.1515;
        distance = distance * 1.609344; // Convert miles to kilometers

        return distance;
    }

    // Request location permissions
    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, PERMISSION_REQUEST_CODE);
    }

    // Initialize location callback
    private void initializeLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        getNearbyBloodBanks(userLocation);
                        stopLocationUpdates();
                    }
                }
            }
        };
    }

    // Request location updates
    private void requestLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    // Stop location updates
    private void stopLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocationCallback();
                requestLocationUpdates();
            }
        }
    }
}
