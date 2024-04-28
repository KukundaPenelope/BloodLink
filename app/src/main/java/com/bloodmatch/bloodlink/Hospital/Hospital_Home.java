package com.bloodmatch.bloodlink.Hospital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.bloodmatch.bloodlink.R;


public class Hospital_Home extends Fragment {


    public Hospital_Home() {
        // Required empty public constructor
    }
//    LinearLayout logoLayout = findViewById(R.id.logo);
//    Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
//logoLayout.startAnimation(breathingAnimation);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hospital__home, container, false);
    }
}