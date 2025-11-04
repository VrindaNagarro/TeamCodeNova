package com.nagarro.driven.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readJsonFile(String filePath, Class<T> clazz) {
        try {
            return mapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to read JSON file: " + filePath, e);
        }
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to convert object to JSON", e);
        }
    }
}
