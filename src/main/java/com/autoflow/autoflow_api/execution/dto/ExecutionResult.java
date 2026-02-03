package com.autoflow.autoflow_api.execution.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExecutionResult {
    private Long workflowId;
    private String status; // SUCCESS / FAILED
    private List<StepRunResult> steps;
}
