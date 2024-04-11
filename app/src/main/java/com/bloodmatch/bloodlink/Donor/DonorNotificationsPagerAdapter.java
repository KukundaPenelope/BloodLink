package com.bloodmatch.bloodlink.Donor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bloodmatch.bloodlink.Patient.CancelledNotificationsFragment;
import com.bloodmatch.bloodlink.Patient.New_Notifications;
import com.bloodmatch.bloodlink.Patient.NotificationType;

public class DonorNotificationsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;
    private NotificationType notificationType;

    public DonorNotificationsPagerAdapter(FragmentManager supportFragmentManager, NotificationType notificationType) {
        super(supportFragmentManager);
        this.notificationType = notificationType;

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return New_Notifications.newInstance();
            case 1:
                return ApprovedNotificationsFragment.newInstance();
            case 2:
                return CancelledNotificationsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "New";
            case 1:
                return "Approved";
            case 2:
                return "Cancelled";
            default:
                return null;
        }
    }
}
