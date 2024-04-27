package com.bloodmatch.bloodlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bloodmatch.bloodlink.BloodBank.BloodBank_Navigation;
import com.bloodmatch.bloodlink.Donor.Donor_Navigation;
import com.bloodmatch.bloodlink.Hospital.Hospital_Navigation;
import com.bloodmatch.bloodlink.Patient.Patient_Navigation;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Simulating splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToMainActivity2();
            }
        }, SPLASH_DURATION);
    }

    private void navigateToMainActivity2() {
        // Check user type and redirect accordingly
        SharedPreferences sharedPreferences = getSharedPreferences("BloodSync", MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", null);

        Intent intent;
        if (userType != null) {
            switch (userType) {
                case "donor":
                    intent = new Intent(MainActivity.this, Donor_Navigation.class);
                    break;
                case "patient":
                    intent = new Intent(MainActivity.this, Patient_Navigation.class);
                    break;
                case "hospital":
                    intent = new Intent(MainActivity.this, Hospital_Navigation.class);
                    break;
                case "bloodBanks":
                    intent = new Intent(MainActivity.this, BloodBank_Navigation.class);
                    break;
                default:
                    // If user type is unknown, redirect to a default activity or login screen
                    intent = new Intent(MainActivity.this, MainActivity2.class);
                    break;
            }
        } else {
            // If user type is not determined, redirect to the onboarding or login screen
            intent = new Intent(MainActivity.this, MainActivity2.class);
        }

        startActivity(intent);
        finish();
    }

}
