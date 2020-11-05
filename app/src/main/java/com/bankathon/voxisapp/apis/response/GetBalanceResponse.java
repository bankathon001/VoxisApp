package com.bankathon.voxisapp.apis.response;

import com.google.gson.annotations.SerializedName;

public class GetBalanceResponse {
    @SerializedName("body")
    private int balance;
    private boolean success;
    private long serverTimeStamp;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getServerTimeStamp() {
        return serverTimeStamp;
    }

    public void setServerTimeStamp(long serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }
}
