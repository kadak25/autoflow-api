package com.autoflow.autoflow_api.execution.util;

import java.util.Map;

public class TemplateResolver {

    public static String resolve(String text, Map<String, Object> ctx) {

        if (text == null) return null;

        String result = text;

        for (var e : ctx.entrySet()) {
            result = result.replace(
                    "{{" + e.getKey() + "}}",
                    String.valueOf(e.getValue())
            );
        }

        return result;
    }
}
