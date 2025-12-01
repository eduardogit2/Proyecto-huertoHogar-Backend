package com.example.HuertoHogar.dto.auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Integer id; 
    private String token;
    private String email;
    private String nombre;
    private String role;
    private Integer puntos;
}