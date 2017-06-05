package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataProcessingRequest {

    private String content;

    public DataProcessingRequest() {
    }

    public DataProcessingRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public DataProcessingRequest setContent(String content) {
        this.content = content;
        return this;
    }

}
