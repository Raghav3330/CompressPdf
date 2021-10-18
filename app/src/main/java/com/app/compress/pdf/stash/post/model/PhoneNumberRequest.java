package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sajal.Jain1 on 12-02-2018.
 */

public class PhoneNumberRequest {

    @SerializedName("userID")
    @Expose
    private String userId;

    @SerializedName("phone_no")
    @Expose
    private String phoneNo;


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    @Override
    public String toString() {
        return "PhoneNumberRequest{" +
                "userId='" + userId + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
