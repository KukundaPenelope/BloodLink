package com.bloodmatch.bloodlink.BloodBank;

public class BloodBanks {
    private String name;
    private String district;
    private String address;
    private String contact;

    public BloodBanks() {
        // Default constructor required for Firebase
    }

    public BloodBanks(String name, String district, String address, String contact) {
        this.name = name;
        this.district = district;
        this.address = address;
        this.contact = contact;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}