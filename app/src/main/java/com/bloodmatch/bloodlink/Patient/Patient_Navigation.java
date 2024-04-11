package com.bloodmatch.bloodlink.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.MainActivity3;
import com.bloodmatch.bloodlink.R;
import com.bloodmatch.bloodlink.databinding.ActivityPatientNavigationBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Patient_Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityPatientNavigationBinding binding;
    private DrawerLayout drawerLayout;
    Toolbar bar;
    private DatabaseReference usersRef;

    ActionBarDrawerToggle toggle;
    MenuItem selectedDrawerItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        showDrawerLayoutFragment(new Patient_Home());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, bar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Get the header view of the navigation drawer
        View headerView = navigationView.getHeaderView(0);

        TextView userNameTextView = headerView.findViewById(R.id.userNameTextView);
        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Patients");

// Get the current user's ID from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String patientId = currentUser.getUid();

            usersRef.child(patientId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fname = dataSnapshot.child("email").getValue(String.class);
//                        String lname = dataSnapshot.child("lastname").getValue(String.class);

//                        String fullName = fname + " " + lname;
                        userNameTextView.setText(fname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Patient_Navigation.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userNameTextView.setText("User not authenticated");
        }


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            MenuItem bottomNavigationItem = binding.bottomNavigationView.getMenu().findItem(itemID);
            highlightSelectedItem(itemID);
            if (itemID == R.id.home) {
                showDrawerLayoutFragment(new Patient_Home());
            } else if (itemID == R.id.search) {
                showDrawerLayoutFragment(new LocateBloodBanksFragment());
            } else if (itemID == R.id.notifications) {
                showDrawerLayoutFragment(new PatientNotifications());
            } else if (itemID == R.id.account) {
                showDrawerLayoutFragment(new PatientAccount());
            }
            return true;
        });
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                binding.bottomNavigationView.setVisibility((int) (View.VISIBLE - slideOffset));
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
        if (bottomNavigationItem == null) {
            binding.bottomNavigationView.setVisibility(View.GONE);

        } else {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationItem.setChecked(true); // Select the item in the bottom navigation
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

        } else if (itemId == R.id.prof) {
            Intent intent = new Intent(Patient_Navigation.this, Profile.class);
            startActivity(intent);

        } else if (itemId == R.id.nav_logoout) {
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

}
