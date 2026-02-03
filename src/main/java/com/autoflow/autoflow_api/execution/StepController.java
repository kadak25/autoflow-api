package com.autoflow.autoflow_api.execution;

import com.autoflow.autoflow_api.execution.dto.StepCreateRequest;
import com.autoflow.autoflow_api.execution.dto.StepResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @PostMapping
    public StepResponse create(@Valid @RequestBody StepCreateRequest req) {
        return stepService.create(req);
    }

    @GetMapping("/workflow/{workflowId}")
    public List<StepResponse> getByWorkflow(@PathVariable Long workflowId) {
        return stepService.listByWorkflow(workflowId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        stepService.delete(id);
        return "Step deleted";
    }
}
