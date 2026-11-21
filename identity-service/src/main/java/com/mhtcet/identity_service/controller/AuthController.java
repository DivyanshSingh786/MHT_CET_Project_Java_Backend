package com.mhtcet.identity_service.controller;

import com.mhtcet.identity_service.config.JwtService;
import com.mhtcet.identity_service.dto.AuthRequest;
import com.mhtcet.identity_service.dto.AuthResponse;
import com.mhtcet.identity_service.model.UserCredential;
import com.mhtcet.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        UserCredential user = UserCredential.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role().toUpperCase() : "STUDENT")
                .build();
        repository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        UserCredential user = repository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getRole()));
    }
}
