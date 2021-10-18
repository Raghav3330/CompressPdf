package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Ananda.Kumar on 21-09-2017.
 */

public class PostsRequest {

    @SerializedName("userID")
    @Expose
    private String userId;

    @SerializedName("lastHitTime")
    @Expose
    private long lastTimeStamp;

    @SerializedName("offset")
    @Expose
    private long postShown;

    @SerializedName("limit")
    @Expose
    private int limit;

    @SerializedName("lang")
    @Expose
    private String language;

    @SerializedName("feedType")
    @Expose
    private String feedType;

    @SerializedName("postCat")
    @Expose
    private String[] postCat;


    @SerializedName("postOffset")
    @Expose
    private int postOffset;


    @SerializedName("vidOffset")
    @Expose
    private int vidOffset;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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


    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public void setPostShown(long postShown) {
        this.postShown = postShown;
    }

    public void setPostOffset(int postOffset) {
        this.postOffset = postOffset;
    }

    public void setVidOffset(int vidOffset) {
        this.vidOffset = vidOffset;
    }

    public void setPostCat(String[] postCat) {
        this.postCat = postCat;
    }

    @Override
    public String toString() {
        return "PostsRequest{" +
                "userId='" + userId + '\'' +
                ", lastTimeStamp=" + lastTimeStamp +
                ", postShown=" + postShown +
                ", limit=" + limit +
                ", language='" + language + '\'' +
                ", feedType='" + feedType + '\'' +
                ", postCat=" + Arrays.toString(postCat) +
                ", postOffset=" + postOffset +
                ", vidOffset=" + vidOffset +
                '}';
    }

    public String[] getPostCat() {
        return postCat;
    }


}
