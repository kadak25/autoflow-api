package com.autoflow.autoflow_api.auth;
import com.autoflow.autoflow_api.auth.dto.LoginRequest;
import com.autoflow.autoflow_api.auth.jwt.JwtService;
import com.autoflow.autoflow_api.config.SecurityBeans;
import com.autoflow.autoflow_api.user.User;
import com.autoflow.autoflow_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SecurityBeans passwordEncoder;


    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request){
        authService.register(request);
        return "User registered";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token, "Bearer");
    }



}
