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

import com.bloodmatch.bloodlink.Donor.Donor;
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

    private void addMarkersForNearbyBloodDonors(List<Donor> nearbyBloodDonors) {
        for (Donor donor : nearbyBloodDonors) {
            LatLng location = getLocationFromAddress(geocoder, donor.getLocation());
            if (location != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(donor.getName())
                        .snippet(donor.getLocation())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            }
        }
    }

    private void getNearbyBloodDonors(LatLng userLocation) {
        List<Donor> nearbyBloodDonors = new ArrayList<>();

        // Query donors collection in Firestore
        firestore.collection("donors")
                .get()
                .addOnCompleteListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.isSuccessful()) {
                  DocumentSnapshot document = queryDocumentSnapshots.getResult().getDocuments().get(0);
//                            // Convert Firestore document to Donor object
                            Donor donor = document.toObject(Donor.class);

                            // Convert address to LatLng
                            LatLng donorLocation = getLocationFromAddress(geocoder, document.getString("location"));

                            if (donorLocation != null) {
                                // Calculate distance between user location and donor location
                                double distance = calculateDistance(userLocation, donorLocation);

                                // Check if donor is within 500 meters
                                if (distance <= 10000) {
                                    nearbyBloodDonors.add(donor);
                                }
                            }
                            else {
                                Log.d("Maps_Activity", "Error getting blood donors: " );
                            }
                        }
                else {
                    Log.d("Maps_Activity", "Error getting blood donors: " );
                }
                        // Add markers for nearby blood donors on the map
                        addMarkersForNearbyBloodDonors(nearbyBloodDonors);

                    });
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
                        getNearbyBloodDonors(userLocation);
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
