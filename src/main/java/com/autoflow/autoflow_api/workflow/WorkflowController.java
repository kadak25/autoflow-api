package com.autoflow.autoflow_api.workflow;

import com.autoflow.autoflow_api.workflow.dto.WorkflowCreateRequest;
import com.autoflow.autoflow_api.workflow.dto.WorkflowResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService service;

    @PostMapping
    public WorkflowResponse create(@Valid @RequestBody WorkflowCreateRequest req) {
        return service.create(req);
    }


    @GetMapping
    public List<WorkflowResponse> list(@RequestParam(required = false) Long ownerUserId) {
        if (ownerUserId == null) {

            return service.listMine();
        }
        return service.listByOwner(ownerUserId);
    }


    @GetMapping("/me")
    public List<WorkflowResponse> myWorkflows() {
        return service.listMine();
    }

    @GetMapping("/{id}")
    public WorkflowResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Workflow deleted";
    }
}
