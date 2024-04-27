package com.bloodmatch.bloodlink.Hospital;

import java.util.Map;

public class Hospital {
    private String address;
    private String contact;
    private String created;
    private String created_by;
    private String district;
    private String email;
    private String hospital_id;
    private String name;


    public Hospital() {
        // Default constructor required for Firestore
    }

    public Hospital(String address, String contact, String created, String created_by, String district, String email, String hospital_id, String name) {
        this.address = address;
        this.contact = contact;
        this.created = created;
        this.created_by = created_by;
        this.district = district;
        this.email = email;
        this.hospital_id = hospital_id;
        this.name = name;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}