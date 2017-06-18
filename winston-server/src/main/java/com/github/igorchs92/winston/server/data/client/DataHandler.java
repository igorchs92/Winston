package com.github.igorchs92.winston.server.data.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 7-6-2017.
 */
public class DataHandler {

    public static Map<String, String> tokenize(String content) {
        Map<String, String> tokens = new HashMap<>();
        String[] greetActions = {"hi", "hello", "good day", "good evening", "good morning"};
        Arrays.stream(greetActions).forEach(s -> {
            if (content.toLowerCase().contains(s)) {
                tokens.put("greeting", s);
            }});

        return tokens;
    }
}
