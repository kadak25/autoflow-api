package com.autoflow.autoflow_api.execution;

import com.autoflow.autoflow_api.execution.dto.ExecutionResult;
import com.autoflow.autoflow_api.execution.dto.StepRunResult;
import com.autoflow.autoflow_api.execution.executors.StepExecutor;
import com.autoflow.autoflow_api.execution.executors.StepExecutorRegistry;
import com.autoflow.autoflow_api.user.User;
import com.autoflow.autoflow_api.user.UserRepository;
import com.autoflow.autoflow_api.workflow.Workflow;
import com.autoflow.autoflow_api.workflow.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkflowExecutionService {

    private final WorkflowRepository workflowRepository;
    private final StepRepository stepRepository;
    private final UserRepository userRepository;


    private final StepExecutorRegistry registry;


    Map<String, Object> context = new HashMap<>();

    public ExecutionResult run(Long workflowId) {
        User me = getCurrentUser();


        Workflow wf = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow not found"));

        Long ownerId = wf.getOwner() == null ? null : wf.getOwner().getId();
        Long myId = me.getId();

        if (ownerId == null || myId == null || ownerId.longValue() != myId.longValue()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to run this workflow");
        }

        List<Step> steps = stepRepository.findByWorkflowIdOrderByOrderIndexAscIdAsc(workflowId);

        List<StepRunResult> results = new ArrayList<>();

        for (Step step : steps) {

            StepRunResult r = runSingleStep(step, context);

            results.add(r);

            if ("FAILED".equals(r.getStatus())) {
                return ExecutionResult.builder()
                        .workflowId(workflowId)
                        .status("FAILED")
                        .steps(results)
                        .build();
            }

            context.put("step" + step.getId() + ".output", r.getOutput());
            context.put("step" + step.getId() + ".httpStatus", r.getHttpStatus());
        }
        return ExecutionResult.builder()
                .workflowId(workflowId)
                .status("SUCCESS")
                .steps(results)
                .build();


    }


    private StepRunResult runSingleStep(Step step, Map<String, Object> context)
    {
        try {
            StepType type = step.getType();
            if (type == null) return failed(step, "Step type is null");

            StepExecutor ex = registry.get(type);
            return ex.execute(step, context);

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

    private StepRunResult failed(Step step, String msg) {
        return StepRunResult.builder()
                .stepId(step.getId())
                .stepName(step.getName())
                .type(step.getType())
                .status("FAILED")
                .error(msg)
                .build();
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}
