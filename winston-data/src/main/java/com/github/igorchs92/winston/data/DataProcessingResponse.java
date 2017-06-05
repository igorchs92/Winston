package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataProcessingResponse {

    private String content;

    public DataProcessingResponse() {
    }

    public DataProcessingResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public DataProcessingResponse setContent(String content) {
        this.content = content;
        return this;
    }

}
