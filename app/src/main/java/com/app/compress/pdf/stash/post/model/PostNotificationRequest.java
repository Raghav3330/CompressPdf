package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostNotificationRequest {

    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("postIDArr")
    @Expose
    private String postIDArr;
    @SerializedName("catCheck")
    @Expose
    private String catCheck;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostIDArr() {
        return postIDArr;
    }

    public void setPostIDArr(String postIDArr) {
        this.postIDArr = postIDArr;
    }

    public String getCatCheck() {
        return catCheck;
    }

    public void setCatCheck(String catCheck) {
        this.catCheck = catCheck;
    }

}