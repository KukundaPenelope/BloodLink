package com.bloodmatch.bloodlink.BloodBank;

// BloodAmount.java
public class BloodAmount {
    private String APlus;
    private String AMinus;
    private String BPlus;
    private String BMinus;
    private String ABPlus;
    private String ABMinus;
    private String OPlus;
    private String OMinus;

    public BloodAmount() {
        // Empty constructor needed for Firestore deserialization
    }

    public BloodAmount(String APlus, String AMinus, String BPlus, String BMinus, String ABPlus, String ABMinus, String OPlus, String OMinus) {
        this.APlus = APlus;
        this.AMinus = AMinus;
        this.BPlus = BPlus;
        this.BMinus = BMinus;
        this.ABPlus = ABPlus;
        this.ABMinus = ABMinus;
        this.OPlus = OPlus;
        this.OMinus = OMinus;
    }

    public String getAPlus() {
        return APlus;
    }

    public void setAPlus(String APlus) {
        this.APlus = APlus;
    }

    public String getAMinus() {
        return AMinus;
    }

    public void setAMinus(String AMinus) {
        this.AMinus = AMinus;
    }

    public String getBPlus() {
        return BPlus;
    }

    public void setBPlus(String BPlus) {
        this.BPlus = BPlus;
    }

    public String getBMinus() {
        return BMinus;
    }

    public void setBMinus(String BMinus) {
        this.BMinus = BMinus;
    }

    public String getABPlus() {
        return ABPlus;
    }

    public void setABPlus(String ABPlus) {
        this.ABPlus = ABPlus;
    }

    public String getABMinus() {
        return ABMinus;
    }

    public void setABMinus(String ABMinus) {
        this.ABMinus = ABMinus;
    }

    public String getOPlus() {
        return OPlus;
    }

    public void setOPlus(String OPlus) {
        this.OPlus = OPlus;
    }

    public String getOMinus() {
        return OMinus;
    }

    public void setOMinus(String OMinus) {
        this.OMinus = OMinus;
    }
}