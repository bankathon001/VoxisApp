package com.bankathon.voxisapp.apis.response;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("body")
    private Object body;
    private boolean success;
    private long serverTimeStamp;

    public Object getBody() {
        return body;
    }

    public void setBody(Object object) {
        this.body = object;
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
