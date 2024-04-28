package com.bloodmatch.bloodlink.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bloodmatch.bloodlink.BloodBank.Patient_Locator;
import com.bloodmatch.bloodlink.BloodBank.LocateBloodBanks;
import com.bloodmatch.bloodlink.DonationSites.MapsActivity;
import com.bloodmatch.bloodlink.Donor.LocateBloodDonors;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DatabaseReference;

public class Patient_Home extends Fragment {

    private EditText nameEditText;
    private DatabaseReference usersRef;
    CardView findBloodMatch,locate,finddonors, about;

    public Patient_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient__home, container, false);
        finddonors = rootView.findViewById(R.id.donor);
        locate = rootView.findViewById(R.id.locat);
        findBloodMatch = rootView.findViewById(R.id.bloodmatch);
        about = rootView.findViewById(R.id.abouts);

        finddonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocateBloodDonors.class);
                startActivity(intent);
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocateBloodBanks.class);
                startActivity(intent);
            }
        });
        findBloodMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutDonation.class);
                startActivity(intent);
            }
        });

        // Rest of your code...

        return rootView;
    }
}
