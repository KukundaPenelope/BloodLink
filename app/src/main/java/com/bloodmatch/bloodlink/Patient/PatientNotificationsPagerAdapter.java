package com.bloodmatch.bloodlink.Patient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PatientNotificationsPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 4;
    private NotificationType notificationType;

    public PatientNotificationsPagerAdapter(FragmentManager fm, NotificationType notificationType) {
        super(fm);
        this.notificationType = notificationType;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return New_Notifications.newInstance();
            case 1:
                return InProgressNotificationsFragment.newInstance();
            case 2:
                return FinishedNotificationsFragment.newInstance();
            case 3:
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
                return "In Progress";
            case 2:
                return "Finished";
            case 3:
                return "Cancelled";
            default:
                return null;
        }
    }
}

