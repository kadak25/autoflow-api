package com.autoflow.autoflow_api.execution.dto;

import com.autoflow.autoflow_api.execution.StepType;
import lombok.Builder;

@Builder
public record StepResponse(
        Long id,
        Long workflowId,
        String name,
        StepType type,
        Integer orderIndex,
        String config
) {}
