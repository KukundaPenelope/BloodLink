package com.bloodmatch.bloodlink;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToMainActivity2();
            }
        }, SPLASH_DURATION);
    }

    private void navigateToMainActivity2() {
        startActivity(new Intent(MainActivity.this, MainActivity2.class));
        finish();
    }
}
