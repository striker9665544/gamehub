package com.yatzee.yatzee_api.controller;

import com.yatzee.yatzee_api.dto.AuthRequest;
import com.yatzee.yatzee_api.dto.AuthResponse;
import com.yatzee.yatzee_api.dto.RegisterRequest;
import com.yatzee.yatzee_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
    	System.out.println("Register API hit with: " + request.getEmail());
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/guest")
    public AuthResponse guestLogin() {
        return authService.guestLogin();
    }
    
    @GetMapping("/test")
    public String testConnection() {
        return "Backend is working with DB!";
    }

}
