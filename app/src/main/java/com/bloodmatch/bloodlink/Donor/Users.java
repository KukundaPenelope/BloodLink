package com.bloodmatch.bloodlink.Donor;

public class Users {
    private String userId;
    private String address;
    private String email;
    private String name;
    private String district, contact;

    private String role;

    public Users(String userId, String address, String email, String name, String district, String contact, String role) {
        this.userId = userId;
        this.address = address;
        this.email = email;
        this.name = name;
        this.district = district;
        this.contact = contact;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
