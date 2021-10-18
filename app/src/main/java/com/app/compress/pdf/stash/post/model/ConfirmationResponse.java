package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ananda.Kumar on 21-09-2017.
 */

public class ConfirmationResponse {

    @SerializedName("response")
    @Expose
    private int responseCode;

    @SerializedName("message")
    @Expose
    private String message;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ConfirmationResponse{" +
                "responseCode=" + responseCode +
                ", message='" + message + '\'' +
                '}';
    }
}
