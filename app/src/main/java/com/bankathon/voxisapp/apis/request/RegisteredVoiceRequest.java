package com.bankathon.voxisapp.apis.request;

public class RegisteredVoiceRequest {


    private String mobileNumber;

    private String base64EncodeSpeech;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getBase64EncodeSpeech() {
        return base64EncodeSpeech;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setBase64EncodeSpeech(String base64EncodeSpeech) {
        this.base64EncodeSpeech = base64EncodeSpeech;
    }
}
