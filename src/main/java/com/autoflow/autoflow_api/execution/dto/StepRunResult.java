package com.autoflow.autoflow_api.execution.dto;

import com.autoflow.autoflow_api.execution.StepType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepRunResult {
    private Long stepId;
    private String stepName;
    private StepType type;

    private String status;     // SUCCESS / FAILED
    private Integer httpStatus;
    private String output;     // response body veya “Slept 1000ms”
    private String error;      // hata varsa

    private Long durationMs;

}
