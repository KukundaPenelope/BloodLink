package com.bloodmatch.bloodlink.BloodBank;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bloodmatch.bloodlink.Hospital.Hospital;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Maps_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng userLocation;
    private Geocoder geocoder;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

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

    private void addBloodBanksToMap() {
        // Initialize the Geocoder
        geocoder = new Geocoder(this, Locale.getDefault());

        // Retrieve the user's location from Firestore
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("patients").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Patient patient = document.toObject(Patient.class);
                        if (patient != null) {
                            String hospitalId = patient.getHospital_id();

                            // Look for the hospital with the specified ID in Firestore
                            firestore.collection("hospital").document(hospitalId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot hospitalDocument = task.getResult();
                                        if (hospitalDocument.exists()) {
                                            // Retrieve the hospital's data
                                            Hospital hospital = hospitalDocument.toObject(Hospital.class);
                                            if (hospital != null) {
                                                String hospitalAddress = hospital.getAddress();
                                                String hospitalName = hospital.getName();

                                                // Convert the hospital's address to coordinates
                                                LatLng hospitalLocation = getLocationFromAddress(geocoder, hospitalAddress);
                                                if (hospitalLocation != null) {
                                                    mMap.addMarker(new MarkerOptions()
                                                            .position(hospitalLocation)
                                                            .title(hospitalName)
                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                                    // Query nearby blood banks from Firestore and add markers to the map
                                                    List<BloodBanks> bloodBanks = getNearbyBloodBanks(hospitalLocation);
                                                    for (BloodBanks bloodBank : bloodBanks) {
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
                                            }
                                        }
                                    } else {
                                        Log.d("Maps_Activity", "Error getting hospital document", task.getException());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.d("Maps_Activity", "Error getting patient document", task.getException());
                }
            }
        });
    }

    // Helper method to convert address to LatLng
    private LatLng getLocationFromAddress(Geocoder geocoder, String address) {
        try {
            List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to get nearby blood banks from Firestore
    private List<BloodBanks> getNearbyBloodBanks(LatLng userLocation) {
        return new ArrayList<>(); // Placeholder for now
    }


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

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, PERMISSION_REQUEST_CODE);
    }

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
                        addBloodBanksToMap();
                        stopLocationUpdates();
                    }
                }
            }
        };
    }

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
