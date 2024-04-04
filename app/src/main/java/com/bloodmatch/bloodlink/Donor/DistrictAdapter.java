package com.bloodmatch.bloodlink.Donor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DistrictAdapter extends ArrayAdapter<String> {
    private List<String> districts;

    public DistrictAdapter(Context context, List<String> districts) {
        super(context, 0, districts);
        this.districts = districts;
    }

    public void updateData(List<String> newDistricts) {
        districts.clear();
        districts.addAll(newDistricts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Get the district at the specified position
        String district = getItem(position);

        // Bind the district data to the view
        TextView districtTextView = convertView.findViewById(android.R.id.text1);
        districtTextView.setText(district);

        return convertView;
    }
}