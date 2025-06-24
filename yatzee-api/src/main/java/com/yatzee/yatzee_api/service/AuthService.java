package com.yatzee.yatzee_api.service;

import com.yatzee.yatzee_api.dto.AuthRequest;
import com.yatzee.yatzee_api.dto.AuthResponse;
import com.yatzee.yatzee_api.dto.RegisterRequest;
import com.yatzee.yatzee_api.entity.Role;
import com.yatzee.yatzee_api.entity.User;
import com.yatzee.yatzee_api.repository.UserRepository;
import com.yatzee.yatzee_api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setGuest(false);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        authManager.authenticate(authToken);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse guestLogin() {
        User guest = new User();
        guest.setUsername("guest_" + UUID.randomUUID().toString().substring(0, 8));
        guest.setEmail("guest_" + UUID.randomUUID().toString() + "@gamehub.com");
        guest.setPassword(""); // no password
        guest.setRole(Role.ROLE_GUEST);
        guest.setGuest(true);
        guest.setCreatedAt(LocalDateTime.now());

        userRepository.save(guest);
        String token = jwtService.generateToken(guest);
        return new AuthResponse(token);
    }
}
