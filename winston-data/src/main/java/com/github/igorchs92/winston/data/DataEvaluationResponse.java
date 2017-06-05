package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataEvaluationResponse {

    private String content;

    public DataEvaluationResponse() {
    }

    public DataEvaluationResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public DataEvaluationResponse setContent(String content) {
        this.content = content;
        return this;
    }

}
