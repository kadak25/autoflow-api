package com.autoflow.autoflow_api.execution;

import com.autoflow.autoflow_api.execution.dto.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowRunController {

    private final WorkflowExecutionService executionService;

    @PostMapping("/{id}/run")
    public ExecutionResult run(@PathVariable Long id) {
        return executionService.run(id);
    }
}
