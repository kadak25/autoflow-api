package com.autoflow.autoflow_api.execution;

import com.autoflow.autoflow_api.execution.dto.StepCreateRequest;
import com.autoflow.autoflow_api.execution.dto.StepResponse;
import com.autoflow.autoflow_api.user.User;
import com.autoflow.autoflow_api.user.UserRepository;
import com.autoflow.autoflow_api.workflow.Workflow;
import com.autoflow.autoflow_api.workflow.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;
    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository;

    // =============== CREATE ===============
    public StepResponse create(StepCreateRequest req) {
        User me = getCurrentUser();

        Workflow wf = workflowRepository.findById(req.workflowId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow not found"));

        // owner check
        if (wf.getOwner() == null || wf.getOwner().getId() == null || !wf.getOwner().getId().equals(me.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to modify this workflow");
        }

        Step step = Step.builder()
                .workflow(wf)
                .name(req.name())
                .type(req.type())
                .orderIndex(req.orderIndex())
                .config(req.config())
                .build();

        Step saved = stepRepository.save(step);
        return toResponse(saved);
    }

    // =============== LIST ===============
    public List<StepResponse> listByWorkflow(Long workflowId) {
        User me = getCurrentUser();

        Workflow wf = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow not found"));

        if (wf.getOwner() == null || wf.getOwner().getId() == null || !wf.getOwner().getId().equals(me.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this workflow");
        }

        return stepRepository.findByWorkflowIdOrderByOrderIndexAscIdAsc(workflowId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =============== DELETE ===============
    public void delete(Long stepId) {
        User me = getCurrentUser();

        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Step not found"));

        Workflow wf = step.getWorkflow();

        if (wf.getOwner() == null || wf.getOwner().getId() == null || !wf.getOwner().getId().equals(me.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this step");
        }

        stepRepository.deleteById(stepId);
    }

    // =============== MAPPER ===============
    private StepResponse toResponse(Step s) {
        Long wfId = (s.getWorkflow() == null) ? null : s.getWorkflow().getId();
        return StepResponse.builder()
                .id(s.getId())
                .workflowId(wfId)
                .name(s.getName())
                .type(s.getType())
                .orderIndex(s.getOrderIndex())
                .config(s.getConfig())
                .build();
    }

    // =============== AUTH ===============
    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}
