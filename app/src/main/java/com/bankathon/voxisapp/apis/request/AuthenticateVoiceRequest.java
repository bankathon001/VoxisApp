package com.bankathon.voxisapp.apis.request;


public class AuthenticateVoiceRequest {

    private String mobileNumber;

    private String base64EncodeSpeech;

    private String captcha;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getBase64EncodeSpeech() {
        return base64EncodeSpeech;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setBase64EncodeSpeech(String base64EncodeSpeech) {
        this.base64EncodeSpeech = base64EncodeSpeech;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
