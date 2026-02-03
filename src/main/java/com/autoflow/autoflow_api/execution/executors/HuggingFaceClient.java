package com.autoflow.autoflow_api.execution.executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

public class HuggingFaceClient {

    private final RestClient restClient;
    private final ObjectMapper om;
    private final String apiKey;

    public HuggingFaceClient(RestClient restClient, ObjectMapper om, String apiKey) {
        this.restClient = restClient;
        this.om = om;
        this.apiKey = apiKey;
    }

    @SuppressWarnings("unchecked")
    public String summarize(String model, String text) {
        String url = "https://router.huggingface.co/hf-inference/models/" + model;

        Map<String, Object> payload = Map.of(
                "inputs", text,
                "parameters", Map.of(
                        "max_length", 130,
                        "min_length", 30,
                        "do_sample", false
                )
        );

        String raw = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(payload)
                .retrieve()
                .body(String.class);

        // çoğu zaman: [ { "summary_text": "..." } ]
        try {
            List<Map<String, Object>> arr = om.readValue(raw, List.class);
            if (arr != null && !arr.isEmpty()) {
                Object s = arr.get(0).get("summary_text");
                if (s != null) return s.toString();
            }
        } catch (Exception ignore) {}

        return raw;
    }
}
