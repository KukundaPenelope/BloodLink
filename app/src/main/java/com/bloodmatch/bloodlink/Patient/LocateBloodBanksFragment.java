package com.bloodmatch.bloodlink.Patient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.BloodBank.BloodBankAdapter;
import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocateBloodBanksFragment extends Fragment {
    private RecyclerView recyclerView;
    private BloodBankAdapter adapter;
    private List<BloodBanks> bloodBanks;
    private EditText searchEditText;
    private FirebaseFirestore db;

    private Toolbar backTool;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locate_blood_banks, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.search_district);

        bloodBanks = new ArrayList<>();
        adapter = new BloodBankAdapter(getActivity(), bloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchDistricts();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String district = charSequence.toString();
                filterDistricts(district);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    public void fetchDistricts() {
        db.collection("blood_bank")
                .get()
                .addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        bloodBanks.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BloodBanks bloodBank = document.toObject(BloodBanks.class);
                            bloodBanks.add(bloodBank);
                        }
                        updateBloodBankAdapter(bloodBanks);
                    } else {
                        Toast.makeText(getActivity(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void filterDistricts(String district) {
        List<BloodBanks> filteredDistricts = new ArrayList<>();
        for (BloodBanks bloodBank : bloodBanks) {
            if (bloodBank.getDistrict().toLowerCase().contains(district.toLowerCase())) {
                filteredDistricts.add(bloodBank);
            }
        }
        updateBloodBankAdapter(filteredDistricts);
    }

    private void updateBloodBankAdapter(List<BloodBanks> bloodBanks) {
        BloodBankAdapter bloodBankAdapter = new BloodBankAdapter(getActivity(), bloodBanks);
        recyclerView.setAdapter(bloodBankAdapter);
    }
}
