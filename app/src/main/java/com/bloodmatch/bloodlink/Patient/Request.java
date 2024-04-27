package com.bloodmatch.bloodlink.Patient;

public class Request {
    private String request_id;
    private String donorId;
    private String patient_id;
    private String request_time;
    private String fcmToken;
    private String status;
    private String updated;

    public Request() {
    }

    public Request(String request_id, String donorId, String patient_id, String request_time, String fcmToken, String status) {
        this.request_id = request_id;
        this.donorId = donorId;
        this.patient_id = patient_id;
        this.request_time = request_time;
        this.fcmToken = fcmToken;
        this.status = status;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
