package com.bloodmatch.bloodlink.Donor;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bloodmatch.bloodlink.Patient.NotificationType;
import com.bloodmatch.bloodlink.R;

public class Donor_Notifications extends AppCompatActivity {

    private static final String ARG_NOTIFICATION_TYPE = "notification_type";
    private NotificationType notificationType;

    DonorNotificationsPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;
    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_notifications);
        backTool = findViewById(R.id.notificationstoolbar);
        setSupportActionBar(backTool);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getExtras() != null) {
            notificationType = (NotificationType) getIntent().getSerializableExtra(ARG_NOTIFICATION_TYPE);
        }

        demoCollectionPagerAdapter = new DonorNotificationsPagerAdapter(getSupportFragmentManager(), notificationType);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(demoCollectionPagerAdapter);
    }
}
