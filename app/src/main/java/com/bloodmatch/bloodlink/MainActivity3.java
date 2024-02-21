package com.bloodmatch.bloodlink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bloodmatch.bloodlink.Patient.Patient_Navigation;

public class MainActivity3 extends AppCompatActivity {
    AppCompatButton signin;

    TextView signup,forgot_paswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        signin=findViewById(R.id.sign);
        signup=findViewById(R.id.reg);
        forgot_paswd=findViewById(R.id.forgotpsd);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity3.this, Patient_Navigation.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, SignUP.class);
                startActivity(intent);
            }
        });

        forgot_paswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity3.this, ForgotPassword.class));
            }
        });

    }



    }
