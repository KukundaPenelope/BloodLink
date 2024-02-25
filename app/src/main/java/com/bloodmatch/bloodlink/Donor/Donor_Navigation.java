package com.bloodmatch.bloodlink.Donor;

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

public class Donor_Navigation  extends AppCompatActivity{
    Toolbar bar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_navigation);


    }
}

