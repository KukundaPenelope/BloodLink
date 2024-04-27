package com.bloodmatch.bloodlink.BloodBank;

import static com.bloodmatch.bloodlink.Patient.BloodBankAdapter2.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bloodmatch.bloodlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class Patient_Locator extends AppCompatActivity implements OnMapReadyCallback {
    private final float DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Marker patientMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_my);
        requestLocationPermissions();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Patient_Locator.this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        fetchPatientLocation();
    }

    private void fetchPatientLocation() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String requestId = getIntent().getStringExtra("requestId");
        if (requestId != null) {
            db.collection("requests").document(requestId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String patientLocation = documentSnapshot.getString("patientLocation");
                                if (patientLocation != null && !patientLocation.isEmpty()) {
                                    // Convert the patient's location to LatLng
                                    Geocoder geocoder = new Geocoder(Patient_Locator.this, Locale.getDefault());
                                    try {
                                        Address address = geocoder.getFromLocationName(patientLocation, 1).get(0);
                                        LatLng patientLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                                        addPatientMarker(patientLatLng); // Add marker for patient's location
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(patientLatLng, DEFAULT_ZOOM));
                                    } catch (Exception e) {
                                        showToast("Error fetching patient location: " + e.getMessage());
                                    }
                                } else {
                                    showToast("Patient location not found");
                                }
                            } else {
                                showToast("Request document does not exist");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Error fetching patient location: " + e.getMessage());
                        }
                    });
        } else {
            showToast("Request ID not found");
        }
    }

    private void addPatientMarker(LatLng patientLatLng) {
        if (patientMarker != null) {
            patientMarker.remove();
        }
        patientMarker = mMap.addMarker(new MarkerOptions()
                .position(patientLatLng)
                .title("Patient's Location"));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, proceed with your logic
                fetchPatientLocation();
            } else {
                // Permissions denied, handle this scenario
                // You can show a toast or dialog explaining why you need the permissions and prompt the user again
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
