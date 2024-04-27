package com.bloodmatch.bloodlink.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.Patient.LocateBloodBanksFragment;
import com.bloodmatch.bloodlink.Patient.PatientAccount;
import com.bloodmatch.bloodlink.Patient.PatientNotifications;
import com.bloodmatch.bloodlink.Patient.Patient_Home;
import com.bloodmatch.bloodlink.R;
import com.bloodmatch.bloodlink.databinding.ActivityHospitalNavigationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Hospital_Navigation extends AppCompatActivity {

    ActivityHospitalNavigationBinding binding;
    Toolbar bar;
    MenuItem selectedDrawerItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        replaceFragment(new Hospital_Home());

        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    replaceFragment(new Hospital_Home());
                } else if (itemId == R.id.account) {
                    replaceFragment(new Hospital_Account());
                }
                return true;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }
}