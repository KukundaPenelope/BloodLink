package com.bloodmatch.bloodlink.BloodBank;

import java.util.Map;

public class BloodBank {
    private String name;
    private String contact;
    private String address;
    private Map<String, Long> bloodAmount; // blood_amount field as a map

    public BloodBank() {
        // Default constructor required for Firestore
    }

    // Constructor with all fields
    public BloodBank(String name, String contact, String address, Map<String, Long> bloodAmount) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.bloodAmount = bloodAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Long> getBloodAmount() {
        return bloodAmount;
    }

    public void setBloodAmount(Map<String, Long> bloodAmount) {
        this.bloodAmount = bloodAmount;
    }
}
