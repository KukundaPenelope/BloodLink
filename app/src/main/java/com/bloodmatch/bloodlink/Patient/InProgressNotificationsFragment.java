package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodmatch.bloodlink.R;


public class InProgressNotificationsFragment extends Fragment {
    public static InProgressNotificationsFragment newInstance() {
        return new InProgressNotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_progress_notifications, container, false);
    }
}