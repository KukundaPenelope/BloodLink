package com.bloodmatch.bloodlink.Donor;


public class Donor {
    private String age;

    private String blood_type;
    private String created;
    private String created_by;
    private String dob;

    private String email;
    private String gender;
    private String location;
    private String hospital_id;
    private Boolean emailVerified;

    private String name;
    private String phone_number;
    private String role;
    private String user_id;
    private String donor_id;

    public Donor() {
        // Default constructor required for Firestore
    }

    public Donor(String age, String blood_type, String created, String created_by, String dob, String email, String gender, String location, String hospital_id, Boolean emailVerified, String name, String phone_number, String role, String user_id, String donor_id) {
        this.age = age;
        this.blood_type = blood_type;
        this.created = created;
        this.created_by = created_by;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.location = location;
        this.hospital_id = hospital_id;
        this.emailVerified = emailVerified;
        this.name = name;
        this.phone_number = phone_number;
        this.role = role;
        this.user_id = user_id;
        this.donor_id = donor_id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(String donor_id) {
        this.donor_id = donor_id;
    }
}