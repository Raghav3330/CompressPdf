package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ananda.Kumar on 21-09-2017.
 */

public class ActionRequest {

    @SerializedName("userID")
    @Expose
    private String userId;
    @SerializedName("action")
    @Expose
    private List<Action> actions;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
