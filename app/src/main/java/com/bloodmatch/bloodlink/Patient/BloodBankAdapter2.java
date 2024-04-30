//package com.bloodmatch.bloodlink.Patient;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.provider.Settings;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bloodmatch.bloodlink.BloodBank.BloodAmount;
//import com.bloodmatch.bloodlink.BloodBank.BloodBanks;
//import com.bloodmatch.bloodlink.R;
//
//import java.util.List;
//
//public class BloodBankAdapter2 extends RecyclerView.Adapter<BloodBankAdapter2.BloodBankViewHolder> {
//    private List<BloodBanks> bloodBanks;
//    private Context context;
//    private String patientBloodGroup; // Patient's blood group
//    public static final int PERMISSION_REQUEST_CODE = 1;
//
//    public BloodBankAdapter2(Context context, List<BloodBanks> bloodBanks, String patientBloodGroup) {
//        this.context = context;
//        this.bloodBanks = bloodBanks;
//        this.patientBloodGroup = patientBloodGroup;
//    }
//
//    @NonNull
//    @Override
//    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_matcg, parent, false);
//        return new BloodBankViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position) {
//        final BloodBanks bloodBank = bloodBanks.get(position);
//        holder.bind(bloodBank, patientBloodGroup);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Your onClick implementation here
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return bloodBanks.size();
//    }
//
//    public static class BloodBankViewHolder extends RecyclerView.ViewHolder {
//        private TextView nameTextView;
//        private TextView districtTextView;
//        private TextView addressTextView;
//        private TextView contactTextView;
//        private TextView bloodAvailabilityTextView;
//
//        public BloodBankViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.nameTextView);
//            districtTextView = itemView.findViewById(R.id.districtTextView);
//            addressTextView = itemView.findViewById(R.id.addressTextView);
//            contactTextView = itemView.findViewById(R.id.contactTextView);
//            bloodAvailabilityTextView = itemView.findViewById(R.id.bloodAvailabilityTextView);
//        }
//
//        public void bind(BloodBanks bloodBank, String patientBloodGroup) {
//            nameTextView.setText(bloodBank.getName());
//            districtTextView.setText(bloodBank.getDistrict());
//            addressTextView.setText(bloodBank.getAddress());
//            contactTextView.setText(bloodBank.getContact());
//
//            // Display blood availability for the patient's blood group
//            Long bloodAmount = (Long) bloodBank.getBloodAmount();
//            String bloodAvailability = "";
//
//            switch (patientBloodGroup) {
////                case "A+":
////                    bloodAvailability += "A+: " + bloodAmount.getAPlus() + "\n";
////                    break;
////                case "A-":
////                    bloodAvailability += "A-: " + bloodAmount.getAMinus() + "\n";
////                    break;
////                case "B+":
////                    bloodAvailability += "B+: " + bloodAmount.getBPlus() + "\n";
////                    break;
////                case "B-":
////                    bloodAvailability += "B-: " + bloodAmount.getBMinus() + "\n";
////                    break;
////                case "AB+":
////                    bloodAvailability += "AB+: " + bloodAmount.getABPlus() + "\n";
////                    break;
////                case "AB-":
////                    bloodAvailability += "AB-: " + bloodAmount.getABMinus() + "\n";
////                    break;
////                case "O+":
////                    bloodAvailability += "O+: " + bloodAmount.getOPlus() + "\n";
////                    break;
////                case "O-":
////                    bloodAvailability += "O-: " + bloodAmount.getOMinus() + "\n";
////                    break;
////                default:
////                    break;
//            }
//
//            bloodAvailabilityTextView.setText(bloodAvailability);
//        }
//    }
//}
