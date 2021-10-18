package com.app.compress.pdf.stash.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sajal.Jain1 on 05-10-2017.
 */

public class OEmbedResponse {
    @SerializedName("height")
    @Expose
    private int height;

    public int getHeight() {
        return height;
    }


}
