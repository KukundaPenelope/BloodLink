package com.bloodmatch.bloodlink.BloodBank;

import java.util.Map;

public class BloodBanks {
    private String address;
    private BloodAmount bloodAmount;
    private String contact;
    private String created;
    private String createdBy;
    private String district;
    private String email;
    private String hospitalId;
    private String name;

    public BloodBanks() {
        // Default constructor required for Firestore
    }

    public BloodBanks(String address, BloodAmount bloodAmount, String contact, String created, String createdBy, String district, String email, String hospitalId, String name) {
        this.address = address;
        this.bloodAmount = bloodAmount;
        this.contact = contact;
        this.created = created;
        this.createdBy = createdBy;
        this.district = district;
        this.email = email;
        this.hospitalId = hospitalId;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BloodAmount getBloodAmount() {
        return bloodAmount;
    }

    public void setBloodAmount(BloodAmount bloodAmount) {
        this.bloodAmount = bloodAmount;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
