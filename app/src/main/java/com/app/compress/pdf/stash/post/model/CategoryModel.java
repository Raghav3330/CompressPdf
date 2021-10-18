package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Sajal.Jain1 on 05-03-2018.
 */

public class CategoryModel {
    @SerializedName("cna")
    @Expose
    private String[] cateNames;

    @SerializedName("cof")
    @Expose
    private int cateOffset;

    @SerializedName("clim")
    @Expose
    private int cateLimit;

    @SerializedName("calias")
    @Expose
    private int cateAlias;

    public String[] getCateNames() {
        return cateNames;
    }

    public void setCateNames(String[] cateNames) {
        this.cateNames = cateNames;
    }

    public int getCateOffset() {
        return cateOffset;
    }

    public void setCateOffset(int cateOffset) {
        this.cateOffset = cateOffset;
    }

    public int getCateLimit() {
        return cateLimit;
    }

    public void setCateLimit(int cateLimit) {
        this.cateLimit = cateLimit;
    }

    public int getCateAlias() {
        return cateAlias;
    }

    public void setCateAlias(int cateAlias) {
        this.cateAlias = cateAlias;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "cateNames=" + Arrays.toString(cateNames) +
                ", cateOffset=" + cateOffset +
                ", cateLimit=" + cateLimit +
                ", cateAlias=" + cateAlias +
                '}';
    }
}
