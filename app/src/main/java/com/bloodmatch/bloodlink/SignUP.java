package com.bloodmatch.bloodlink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bloodmatch.bloodlink.Donor.Donor_SignUp;
import com.bloodmatch.bloodlink.Hospital.Hospital_SignUp;
import com.bloodmatch.bloodlink.Patient.Patient_SignUp;

public class SignUP extends AppCompatActivity {

    CardView donor, Patient, hospital,sign_In;
    Toolbar back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        donor =findViewById(R.id.donor);
        Patient=findViewById(R.id.patient);
        hospital =findViewById(R.id.hospital);
        sign_In=findViewById(R.id.sign_in);
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUP.this, Donor_SignUp.class);
                startActivity(intent);
            }

        });
        Patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUP.this, Patient_SignUp.class);
                startActivity(intent);
            }

        });
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUP.this, Hospital_SignUp.class);
                startActivity(intent);
            }

        });
        sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUP.this, MainActivity3.class);
                startActivity(intent);
            }
        });
        back=findViewById(R.id.back);
        setSupportActionBar(back);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
