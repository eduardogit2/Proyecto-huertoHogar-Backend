package com.example.HuertoHogar.services;

import com.example.HuertoHogar.dto.auth.AuthenticationRequest;
import com.example.HuertoHogar.dto.auth.AuthenticationResponse;
import com.example.HuertoHogar.dto.auth.RegisterRequest;
import com.example.HuertoHogar.model.Role;
import com.example.HuertoHogar.model.User;
import com.example.HuertoHogar.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .rut(request.getRut())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .puntos(0)
                .build();
        
        User savedUser = repository.save(user);
        
        var jwtToken = jwtService.generateToken(savedUser);
        
        return AuthenticationResponse.builder()
                .id(savedUser.getId())
                .token(jwtToken)
                .email(savedUser.getEmail())
                .nombre(savedUser.getNombre())
                .role(savedUser.getRole().name())
                .puntos(0)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .email(user.getEmail())
                .nombre(user.getNombre())
                .role(user.getRole().name())
                .puntos(user.getPuntos() != null ? user.getPuntos() : 0)
                .build();
    }
}