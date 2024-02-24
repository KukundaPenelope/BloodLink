package com.bloodmatch.bloodlink.Donor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloodmatch.bloodlink.BloodBank.BloodBankAdapter;
import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
import com.bloodmatch.bloodlink.R;

import java.util.ArrayList;
import java.util.List;

public class Donor_Home extends Fragment {
    private RecyclerView bloodBanksRecyclerView;
    private BloodBankAdapter bloodBankAdapter;

    public Donor_Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donor__home, container, false);


        return view;
    }

}