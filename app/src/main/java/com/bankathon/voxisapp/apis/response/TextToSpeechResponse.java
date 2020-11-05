package com.bankathon.voxisapp.apis.response;

public class TextToSpeechResponse {
    private String base64EncodeSpeech;

    public void setBase64EncodeSpeech(String base64EncodeSpeech) {
        this.base64EncodeSpeech = base64EncodeSpeech;
    }

    public String getBase64EncodeSpeech() {
        return base64EncodeSpeech;
    }
}
