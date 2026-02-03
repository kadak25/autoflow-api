package com.autoflow.autoflow_api.execution.executors;

import com.fasterxml.jackson.databind.ObjectMapper;

public record DelayStepConfig(Long ms) {
    public static DelayStepConfig fromJson(ObjectMapper om, String json) {
        try {
            if (json == null || json.isBlank()) return new DelayStepConfig(1000L);
            return om.readValue(json, DelayStepConfig.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid DELAY step config JSON: " + e.getMessage(), e);
        }
    }
}
