package com.autoflow.autoflow_api.execution.executors;


import com.autoflow.autoflow_api.execution.Step;
import com.autoflow.autoflow_api.execution.StepType;
import com.autoflow.autoflow_api.execution.dto.StepRunResult;

import java.util.Map;

public interface StepExecutor {
        StepType type();
        StepRunResult execute(Step step, Map<String, Object> context);
    }

