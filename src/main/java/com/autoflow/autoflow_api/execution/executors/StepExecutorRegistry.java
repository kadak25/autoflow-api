package com.autoflow.autoflow_api.execution.executors;

import com.autoflow.autoflow_api.execution.StepType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
    public class StepExecutorRegistry {
        private final Map<StepType, StepExecutor> map;

        public StepExecutorRegistry(List<StepExecutor> executors) {
            this.map = executors.stream()
                    .collect(Collectors.toMap(StepExecutor::type, e -> e));
        }

        public StepExecutor get(StepType type) {
            StepExecutor ex = map.get(type);
            if (ex == null) throw new IllegalArgumentException("No executor for type: " + type);
            return ex;
        }
    }


