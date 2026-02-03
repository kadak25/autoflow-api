package com.autoflow.autoflow_api.execution.executors;

import com.autoflow.autoflow_api.execution.Step;
import com.autoflow.autoflow_api.execution.StepType;
import com.autoflow.autoflow_api.execution.dto.StepRunResult;
import com.autoflow.autoflow_api.execution.util.TemplateResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class AiSummaryStepExecutor implements StepExecutor {

    private final ObjectMapper objectMapper;
    private final HuggingFaceClient hf;

    public AiSummaryStepExecutor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        String key = System.getenv("HF_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("HF_API_KEY env variable is missing");
        }
        this.hf = new HuggingFaceClient(RestClient.create(), objectMapper, key);

    }

    @Override
    public StepType type() {
        return StepType.AI_SUMMARY;
    }

    @Override
    public StepRunResult execute(Step step, Map<String, Object> context) {

        try {
            var cfg = AiSummaryStepConfig.fromJson(objectMapper, step.getConfig());

            String input = cfg.input();
            input = TemplateResolver.resolve(input, context);

            if (input == null || input.isBlank()) {
                return StepRunResult.builder()
                        .stepId(step.getId())
                        .stepName(step.getName())
                        .type(step.getType())
                        .status("FAILED")
                        .error("AI_SUMMARY config missing input")
                        .build();
            }

            String summary = hf.summarize(cfg.model(), input);


            // truncate
            String out = summary == null ? null :
                    (summary.length() > cfg.maxChars() ? summary.substring(0, cfg.maxChars()) + "...(truncated)" : summary);

            return StepRunResult.builder()
                    .stepId(step.getId())
                    .stepName(step.getName())
                    .type(step.getType())
                    .status("SUCCESS")
                    .output(out)
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
