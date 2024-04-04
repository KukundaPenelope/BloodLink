package com.bloodmatch.bloodlink.Hospital;

public class Hospial {

    private String hospitalId,  name,  district,  location,  phone,  email,  password;

    public Hospial() {
    }

    public Hospial(String hospitalId, String name, String district, String location, String phone, String email, String password) {
        this.hospitalId=hospitalId;
        this.name=name;
        this.district=district;
        this.location=location;
        this.phone=phone;
        this.email=email;
        this.password=password;

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
