package com.autoflow.autoflow_api.workflow;

import com.autoflow.autoflow_api.user.User;
import com.autoflow.autoflow_api.user.UserRepository;
import com.autoflow.autoflow_api.workflow.dto.WorkflowCreateRequest;
import com.autoflow.autoflow_api.workflow.dto.WorkflowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final UserRepository userRepository;

    public WorkflowResponse create(WorkflowCreateRequest req) {

        User owner = getCurrentUser();

        Workflow wf = Workflow.builder()
                .name(req.getName())
                .description(req.getDescription())
                .owner(owner)
                .build();

        Workflow saved = workflowRepository.save(wf);
        return toResponse(saved);
    }

    public List<WorkflowResponse> listByOwner(Long ownerUserId) {
        if (ownerUserId == null) {
            throw new RuntimeException("ownerUserId is required");
        }

        return workflowRepository.findAllByOwnerIdOrderByIdDesc(ownerUserId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WorkflowResponse> listMine() {
        User me = getCurrentUser();

        return workflowRepository.findAllByOwnerIdOrderByIdDesc(me.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public WorkflowResponse get(Long id) {
        Workflow wf = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        return toResponse(wf);
    }

    public void delete(Long id) {
        if (!workflowRepository.existsById(id)) {
            throw new RuntimeException("Workflow not found");
        }
        workflowRepository.deleteById(id);
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String email = auth.getName(); // DbUserDetailsService -> username=email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    private WorkflowResponse toResponse(Workflow wf) {
        return WorkflowResponse.builder()
                .id(wf.getId())
                .name(wf.getName())
                .description(wf.getDescription())
                .ownerUserId(wf.getOwner().getId())
                .ownerEmail(wf.getOwner().getEmail())
                .build();
    }
}
