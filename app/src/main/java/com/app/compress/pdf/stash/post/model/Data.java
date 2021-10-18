package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ananda.Kumar on 13-10-2017.
 */

public class Data {

    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("lastPostID")
    @Expose
    private int lastPostId;

    @SerializedName("postOffset")
    @Expose
    private int postOffset;

    @SerializedName("vidOffset")
    @Expose
    private int vidOffset;

    @SerializedName("calias")
    @Expose
    private int calias;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(int lastPostId) {
        this.lastPostId = lastPostId;
    }

    public void setPostOffset(int postOffset) {
        this.postOffset = postOffset;
    }

    public void setVidOffset(int vidOffset) {
        this.vidOffset = vidOffset;
    }

    public int getPostOffset() {
        return postOffset;
    }

    public int getVidOffset() {
        return vidOffset;
    }

    public int getCalias() {
        return calias;
    }

    public void setCalias(int calias) {
        this.calias = calias;
    }
}
