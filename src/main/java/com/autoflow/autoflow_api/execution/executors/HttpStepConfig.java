package com.autoflow.autoflow_api.execution.executors;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public record HttpStepConfig(
        String url,
        String method,
        Map<String, String> headers,
        String body
) {
    public static HttpStepConfig fromJson(ObjectMapper om, String json) {
        try {
            if (json == null || json.isBlank()) {
                return new HttpStepConfig(null, "GET", null, null);
            }
            return om.readValue(json, HttpStepConfig.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid HTTP step config JSON: " + e.getMessage(), e);
        }
    }
}
