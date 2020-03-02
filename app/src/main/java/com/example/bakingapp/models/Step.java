package com.example.bakingapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {
    @Expose
    @SerializedName("id")
    private Integer mId;

    @Expose
    @SerializedName("shortDescription")
    private String mDescription;

    @Expose
    @SerializedName("videoURL")
    private String mVideoUrl;

    @Expose
    @SerializedName("thumbnailURL")
    private String mThumbnailUrl;

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public Integer getId() {
        return mId;
    }
}
