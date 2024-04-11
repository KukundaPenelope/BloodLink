package com.bloodmatch.bloodlink.Donor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodmatch.bloodlink.R;
public class ApprovedNotificationsFragment extends Fragment {
    public static ApprovedNotificationsFragment newInstance() {
        return new ApprovedNotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_approved_notifications, container, false);
    }
}