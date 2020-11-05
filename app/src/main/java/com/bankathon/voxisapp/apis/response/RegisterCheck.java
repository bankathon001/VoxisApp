package com.bankathon.voxisapp.apis.response;

import com.google.gson.annotations.SerializedName;

public class RegisterCheck {

    @SerializedName("body")
    private boolean check;
    private boolean success;
    private long serverTimeStamp;

    public boolean getFlag() {
        return check;
    }

    public void setFlag(boolean check) {
        this.check = check;
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
