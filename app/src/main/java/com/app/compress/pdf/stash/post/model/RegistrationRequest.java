package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ananda.Kumar on 21-09-2017.
 */

public class RegistrationRequest {

    @SerializedName("userID")
    @Expose
    private String userId;
    @SerializedName("categories")
    @Expose
    private Map<String, Integer> categories = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Integer> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        categories.put(category, 1);
    }
}
