package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ForYouPostRequest {

    @SerializedName("userID")
    @Expose
    private String userId;

    @SerializedName("lastHitTime")
    @Expose
    private long lastTimeStamp;

    @SerializedName("lang")
    @Expose
    private String language;

    @SerializedName("feedType")
    @Expose
    private String feedType;

    @SerializedName("postCat")
    @Expose
    private ArrayList<CategoryModel> postCat;

    @SerializedName("offset")
    @Expose
    private long postShown;

    @SerializedName("vidOffset")
    @Expose
    private int vidOffset;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public ArrayList<CategoryModel> getPostCat() {
        return postCat;
    }

    public void setPostCat(ArrayList<CategoryModel> postCat) {
        this.postCat = postCat;
    }

    public long getPostShown() {
        return postShown;
    }

    public void setPostShown(long postShown) {
        this.postShown = postShown;
    }

    public int getVidOffset() {
        return vidOffset;
    }

    public void setVidOffset(int vidOffset) {
        this.vidOffset = vidOffset;
    }

    @Override
    public String toString() {
        return "ForYouPostRequest{" +
                "userId='" + userId + '\'' +
                ", lastTimeStamp=" + lastTimeStamp +
                ", language='" + language + '\'' +
                ", feedType='" + feedType + '\'' +
                ", postCat=" + postCat.toString() +
                ", postShown=" + postShown +
                ", vidOffset=" + vidOffset +
                '}';
    }
}
