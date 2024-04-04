package com.bloodmatch.bloodlink.Patient;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodmatch.bloodlink.BloodBank.BloodBankAdapter;
import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
import com.bloodmatch.bloodlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocateBloodBanksFragment extends Fragment {
    private RecyclerView recyclerView;
    private BloodBankAdapter adapter;
    private List<BloodBanks> bloodBanks;
    private EditText searchEditText;
    private DatabaseReference bloodBanksRef;
    private Toolbar backTool;
    private Context mContext;
    public LocateBloodBanksFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locate_blood_banks, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        searchEditText = rootView.findViewById(R.id.search_district);



        // Set up RecyclerView
        bloodBanks = new ArrayList<>();
        adapter = new BloodBankAdapter(requireContext(), bloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Set up Firebase reference
        fetchDistricts();

        // Set up TextWatcher for search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Retrieve entered district name
                String district = charSequence.toString();

                // Query the blood banks by district
                filterDistricts(district);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });

        return rootView;
    }

    public void fetchDistricts() {
        bloodBanksRef = FirebaseDatabase.getInstance().getReference("bloodbanks");
        bloodBanksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bloodBankSnapshot : snapshot.getChildren()) {
                    BloodBanks bloodBank = bloodBankSnapshot.getValue(BloodBanks.class);
                    bloodBanks.add(bloodBank);
                }
                updateBloodBankAdapter(bloodBanks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void filterDistricts(String district) {
        List<BloodBanks> filteredDistricts = new ArrayList<>();
        for (BloodBanks bloodBanks1 : bloodBanks) {
            if (bloodBanks1.getDistrict().toLowerCase().contains(district.toLowerCase())) {
                filteredDistricts.add(bloodBanks1);
            }
        }
        updateBloodBankAdapter(filteredDistricts);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void updateBloodBankAdapter(List<BloodBanks> bloodBanks) {
        adapter = new BloodBankAdapter(mContext, bloodBanks);
        recyclerView.setAdapter(adapter);
    }
}
