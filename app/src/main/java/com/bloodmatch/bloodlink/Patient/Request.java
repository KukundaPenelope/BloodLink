package com.bloodmatch.bloodlink.Patient;

public class Request {
    private String requestId;
    private String donorId;
    private String patientId;
    private String requestTime;

    public Request() {
    }

    public Request(String requestId, String donorId, String patientId, String requestTime) {
        this.requestId = requestId;
        this.donorId = donorId;
        this.patientId = patientId;
        this.requestTime = requestTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}