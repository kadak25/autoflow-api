package com.autoflow.autoflow_api.execution.executors;

import com.fasterxml.jackson.databind.ObjectMapper;

public record AiSummaryStepConfig(
        String model,
        String systemPrompt,
        Integer maxChars,
        String input
) {
    public static AiSummaryStepConfig fromJson(ObjectMapper om, String json) {
        try {
            if (json == null || json.isBlank()) {
                return new AiSummaryStepConfig(
                        "facebook/bart-large-cnn",
                        "Summarize the following text.",
                        800,
                        null
                );
            }
            return om.readValue(json, AiSummaryStepConfig.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid AI_SUMMARY config JSON: " + e.getMessage(), e);
        }
    }
}
