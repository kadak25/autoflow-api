package com.autoflow.autoflow_api.execution.dto;

import java.util.Map;

public record HttpStepConfig(
        String url,
        String method,
        Map<String, String> headers,
        String body,
        Integer timeoutMs
) {}
