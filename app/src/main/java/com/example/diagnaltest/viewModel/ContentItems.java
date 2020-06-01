package com.example.diagnaltest.viewModel;

import com.example.diagnaltest.model.Content;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ContentItems {
    @JsonProperty("content")
    ArrayList<Content> content;

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }
}
