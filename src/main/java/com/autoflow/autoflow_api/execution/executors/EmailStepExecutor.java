package com.autoflow.autoflow_api.execution.executors;

import com.autoflow.autoflow_api.execution.Step;
import com.autoflow.autoflow_api.execution.StepType;
import com.autoflow.autoflow_api.execution.dto.StepRunResult;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailStepExecutor implements StepExecutor {

    @Override
    public StepType type() {
        return StepType.EMAIL;
    }

    @Override
    public StepRunResult execute(Step step, Map<String, Object> context) {

        return StepRunResult.builder()
                .stepId(step.getId())
                .stepName(step.getName())
                .type(step.getType())
                .status("SUCCESS")
                .output("EMAIL step (stub) - will be implemented")
                .build();
    }
}
