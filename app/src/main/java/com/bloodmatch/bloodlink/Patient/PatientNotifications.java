package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bloodmatch.bloodlink.R;

public class PatientNotifications extends Fragment {

    private static final String ARG_NOTIFICATION_TYPE = "notification_type";
    private NotificationType notificationType;

    PatientNotificationsPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;

    public static PatientNotifications newInstance(NotificationType notificationType) {
        PatientNotifications fragment = new PatientNotifications();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION_TYPE, notificationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notificationType = (NotificationType) getArguments().getSerializable(ARG_NOTIFICATION_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        demoCollectionPagerAdapter = new PatientNotificationsPagerAdapter(getChildFragmentManager(), notificationType);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(demoCollectionPagerAdapter);
    }
}