package com.example.diagnaltest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Content {
    @JsonProperty("name")
    private String contentName;
    @JsonProperty("poster-image")
    private String contentPosterImage;

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentPosterImage() {
        return contentPosterImage;
    }

    public void setContentPosterImage(String contentPosterImage) {
        this.contentPosterImage = contentPosterImage;
    }
}
