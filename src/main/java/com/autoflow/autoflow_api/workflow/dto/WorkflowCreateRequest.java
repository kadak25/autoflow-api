package com.autoflow.autoflow_api.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WorkflowCreateRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 1000)
    private String description;


    private Long ownerUserId;
}
