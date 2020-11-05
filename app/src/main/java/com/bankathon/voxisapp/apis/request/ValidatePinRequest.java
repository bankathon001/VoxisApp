package com.bankathon.voxisapp.apis.request;

public class ValidatePinRequest {


    private String mobileNumber;

    private String debitPin;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDebitPin() {
        return debitPin;
    }

    public void setDebitPin(String debitPin) {
        this.debitPin = debitPin;
    }
}
