package com.bloodmatch.bloodlink.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.bloodmatch.bloodlink.R;

public class AboutDonation extends AppCompatActivity {
    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_donation);

        LinearLayout whyDonateLayout = findViewById(R.id.why);
        LinearLayout amIEligibleLayout = findViewById(R.id.eligible);
        LinearLayout donationProcessLayout = findViewById(R.id.donate);
        LinearLayout contactUsLayout = findViewById(R.id.contact);
        backTool = findViewById(R.id.toolbar);
        setSupportActionBar(backTool);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back arrow click
                // You can use onBackPressed() or your custom logic here
                onBackPressed();
            }
        });
        whyDonateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the WhyDonateActivity
                Intent intent = new Intent(AboutDonation.this, WhyDonateActivity.class);
                startActivity(intent);
            }
        });

        amIEligibleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AmIEligibleActivity
                Intent intent = new Intent(AboutDonation.this, AmIEligibleActivity.class);
                startActivity(intent);
            }
        });

        donationProcessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the DonationProcessActivity
                Intent intent = new Intent(AboutDonation.this, DonationProcessActivity.class);
                startActivity(intent);
            }
        });

        contactUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ContactUsActivity
                Intent intent = new Intent(AboutDonation.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
    }
}