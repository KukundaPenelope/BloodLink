package com.bloodmatch.bloodlink.Patient;

public class Request {
    private String requestId;
    private String donorId;
    private String patientId;
    private String requestTime;
    private String fcmToken; // FCM token field
    private String requestStatus; // Request status field

    public Request() {
    }

    public Request(String requestId, String donorId, String patientId, String requestTime, String fcmToken, String requestStatus) {
        this.requestId = requestId;
        this.donorId = donorId;
        this.patientId = patientId;
        this.requestTime = requestTime;
        this.fcmToken = fcmToken;
        this.requestStatus = requestStatus;
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

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
