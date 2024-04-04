package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodmatch.bloodlink.R;


public class FinishedNotificationsFragment extends Fragment {
    public static FinishedNotificationsFragment newInstance() {
        return new FinishedNotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished_notifications, container, false);
    }
}