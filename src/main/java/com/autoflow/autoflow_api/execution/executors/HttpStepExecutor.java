package com.autoflow.autoflow_api.execution.executors;

import com.autoflow.autoflow_api.execution.Step;
import com.autoflow.autoflow_api.execution.StepType;
import com.autoflow.autoflow_api.execution.dto.StepRunResult;
import com.autoflow.autoflow_api.execution.util.TemplateResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpStepExecutor implements StepExecutor {

    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    @Override
    public StepType type() {
        return StepType.HTTP;
    }

    @Override
    public StepRunResult execute(Step step, Map<String, Object> context) {

        HttpStepConfig cfg =
                HttpStepConfig.fromJson(objectMapper, step.getConfig());

        String method = (cfg.method() == null || cfg.method().isBlank())
                ? "GET"
                : cfg.method().toUpperCase();

        String url = cfg.url();

        if (url == null || url.isBlank()) {
            return StepRunResult.builder()
                    .stepId(step.getId())
                    .stepName(step.getName())
                    .type(step.getType())
                    .status("FAILED")
                    .error("HTTP config missing url")
                    .build();
        }

        url = TemplateResolver.resolve(url, context);

        var req = restClient
                .method(HttpMethod.valueOf(method))
                .uri(url);

        if (cfg.headers() != null) {
            for (var e : cfg.headers().entrySet()) {
                req = req.header(e.getKey(), e.getValue());
            }
        }

        if (cfg.body() != null && !cfg.body().isBlank()
                && !"GET".equals(method)) {

            String body =
                    TemplateResolver.resolve(cfg.body(), context);

            req = req
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body);
        }

        var resp = req.retrieve().toEntity(String.class);

        boolean ok = resp.getStatusCode().is2xxSuccessful();

        String body = resp.getBody();
        String out = body == null
                ? null
                : body.substring(0, Math.min(body.length(), 500));

        return StepRunResult.builder()
                .stepId(step.getId())
                .stepName(step.getName())
                .type(step.getType())
                .status(ok ? "SUCCESS" : "FAILED")
                .httpStatus(resp.getStatusCode().value())
                .output(out)
                .build();
    }
}

