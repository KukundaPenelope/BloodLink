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

    public BloodAmount(String aPlus, String aMinus, String bPlus, String bMinus,
                       String abPlus, String abMinus, String oPlus, String oMinus) {
        APlus = aPlus;
        AMinus = aMinus;
        BPlus = bPlus;
        BMinus = bMinus;
        ABPlus = abPlus;
        ABMinus = abMinus;
        OPlus = oPlus;
        OMinus = oMinus;
    }

    public String getAPlus() {
        return APlus;
    }

    public String getAMinus() {
        return AMinus;
    }

    public String getBPlus() {
        return BPlus;
    }

    public String getBMinus() {
        return BMinus;
    }

    public String getABPlus() {
        return ABPlus;
    }

    public String getABMinus() {
        return ABMinus;
    }

    public String getOPlus() {
        return OPlus;
    }

    public String getOMinus() {
        return OMinus;
    }
}