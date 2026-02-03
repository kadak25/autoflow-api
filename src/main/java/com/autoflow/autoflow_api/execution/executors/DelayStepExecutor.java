package com.autoflow.autoflow_api.execution.executors;

import com.autoflow.autoflow_api.execution.Step;
import com.autoflow.autoflow_api.execution.StepType;
import com.autoflow.autoflow_api.execution.executors.DelayStepConfig;
import com.autoflow.autoflow_api.execution.dto.StepRunResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DelayStepExecutor implements StepExecutor {

    private final ObjectMapper objectMapper;

    @Override
    public StepType type() {
        return StepType.DELAY;
    }

    @Override
    public StepRunResult execute(Step step, Map<String, Object> context) {

        try {
            DelayStepConfig cfg = DelayStepConfig.fromJson(objectMapper, step.getConfig());
            long ms = (cfg.ms() == null) ? 1000 : cfg.ms();
            Thread.sleep(ms);

            return StepRunResult.builder()
                    .stepId(step.getId())
                    .stepName(step.getName())
                    .type(step.getType())
                    .status("SUCCESS")
                    .output("Slept " + ms + "ms")
                    .build();

        } catch (Exception e) {
            return StepRunResult.builder()
                    .stepId(step.getId())
                    .stepName(step.getName())
                    .type(step.getType())
                    .status("FAILED")
                    .error(e.getMessage())
                    .build();
        }
    }
}
