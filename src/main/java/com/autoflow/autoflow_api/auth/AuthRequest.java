package com.autoflow.autoflow_api.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String fullName;
}
