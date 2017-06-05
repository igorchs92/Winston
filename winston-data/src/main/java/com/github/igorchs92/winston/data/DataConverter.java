package com.github.igorchs92.winston.data;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Igor on 5-6-2017.
 */
public class DataConverter {
    private ObjectMapper mapper;

    public DataConverter() {
        mapper = new ObjectMapper();
    }

    public String writeValue(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public <T> T readValue(String content, Class<T> type) {
        try {
            return mapper.readValue(content, type);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
