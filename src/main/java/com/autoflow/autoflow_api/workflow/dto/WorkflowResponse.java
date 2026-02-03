package com.autoflow.autoflow_api.workflow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkflowResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerUserId;
    private String ownerEmail;
}
