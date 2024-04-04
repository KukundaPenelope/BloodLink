package com.bloodmatch.bloodlink.Hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
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
import com.bloodmatch.bloodlink.databinding.ActivityPatientNavigationBinding;
import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
import com.bloodmatch.bloodlink.databinding.ActivityNavigationBinding;
import com.bloodmatch.bloodlink.databinding.ActivityPatientNavigationBinding;
import com.google.android.material.navigation.NavigationView;


public class Hospital_Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityHospitalNavigationBinding binding;
    private DrawerLayout drawerLayout;
    Toolbar bar;
    ActionBarDrawerToggle toggle;
    MenuItem selectedDrawerItem= null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalNavigationBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        bar=findViewById(R.id.toolbar);
        showDrawerLayoutFragment(new Patient_Home());
        setSupportActionBar(bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        showDrawerLayoutFragment(new Patient_Home());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, bar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            MenuItem bottomNavigationItem = binding.bottomNavigationView.getMenu().findItem(itemID);
            highlightSelectedItem(itemID);
            if (itemID == R.id.home){
                showDrawerLayoutFragment(new Patient_Home());
            }
            else if (itemID == R.id.search){
                showDrawerLayoutFragment(new LocateBloodBanksFragment());
            }
            else if (itemID == R.id.notifications){
                showDrawerLayoutFragment(new PatientNotifications());
            }
            else if (itemID == R.id.account){
                showDrawerLayoutFragment(new PatientAccount());
            }
            return true;
        });
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                binding.bottomNavigationView.setVisibility((int) (View.VISIBLE -slideOffset));
            }
            public void onDrawerClosed(@NonNull View drawerView) {
                super.onDrawerClosed(drawerView);
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });



    }

    private void highlightSelectedItem(int itemId) {
        if (selectedDrawerItem != null) {
            selectedDrawerItem.setChecked(false);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        selectedDrawerItem = navigationView.getMenu().findItem(itemId);
        if (selectedDrawerItem != null) {
            selectedDrawerItem.setChecked(true);
        }

        MenuItem bottomNavItem = binding.bottomNavigationView.getMenu().findItem(itemId);
        if (bottomNavItem != null) {
            bottomNavItem.setChecked(true);
        }
    }


    private void showDrawerLayoutFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_fragment_container, fragment)
                .commit();
        hideBottomNavigationFragment();

        // Check if the fragment is present in the BottomNavigationView
        int itemId = getBottomNavigationItemId(fragment);
        MenuItem bottomNavigationItem = binding.bottomNavigationView.getMenu().findItem(itemId);

        // Set the visibility state for the BottomNavigationView
        if (bottomNavigationItem != null) {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationItem.setChecked(true); // Select the item in the bottom navigation
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
        }
    }
    private void hideBottomNavigationFragment() {
        Fragment bottomNavFragment = getSupportFragmentManager().findFragmentById(R.id.bottom_nav_fragment_container);
        if (bottomNavFragment != null && bottomNavFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .remove(bottomNavFragment)
                    .commit();
        }
    }
    private int getBottomNavigationItemId(Fragment fragment) {
        if (fragment instanceof Patient_Home) {
            return R.id.home;
        } else if (fragment instanceof LocateBloodBanksFragment) {
            return R.id.search;
        } else if (fragment instanceof PatientNotifications) {
            return R.id.notifications;
        } else if (fragment instanceof PatientAccount) {
            return R.id.account;
        }
        return -1;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        highlightSelectedItem(itemId);
        if (itemId == R.id.home1) {
            showDrawerLayoutFragment(new Patient_Home());
        } else if (itemId == R.id.settings) {
            showDrawerLayoutFragment(new LocateBloodBanksFragment());

        }  else if (itemId == R.id.prof) {
            showDrawerLayoutFragment(new LocateBloodBanksFragment());

        }

        else if (itemId == R.id.nav_logoout) {
            // Create an AlertDialog to confirm logout
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");

            // Add positive button - user confirms logout
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity3.class);
                startActivity(intent);
                finish();
            });

            // Add negative button - user cancels logout
            builder.setNegativeButton("No", (dialog, which) -> {
                // Optionally handle cancel action
            });

            // Show the AlertDialog
            builder.show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}