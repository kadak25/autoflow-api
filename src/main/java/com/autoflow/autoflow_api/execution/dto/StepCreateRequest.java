package com.autoflow.autoflow_api.execution.dto;

import com.autoflow.autoflow_api.execution.StepType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StepCreateRequest(
        @NotNull Long workflowId,
        @NotBlank String name,
        @NotNull StepType type,
        @NotNull Integer orderIndex,
        String config
) {}
