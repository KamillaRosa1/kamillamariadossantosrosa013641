package com.projeto.api_artistas.controller;

import com.projeto.api_artistas.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    // Adicionado para recuperar roles no refresh
    private UserDetailsService userDetailsService; 

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"),
                        loginRequest.get("password")
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String accessToken = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            
            // Recupera os detalhes do usuário para manter as ROLES no novo token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // O novo token terá as mesmas permissões (ADMIN) do anterior
            String newAccessToken = jwtUtil.generateToken(username, userDetails.getAuthorities());
            String newRefreshToken = jwtUtil.generateRefreshToken(username);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);
            
            return ResponseEntity.ok(tokens);
        }
        
        return ResponseEntity.status(401).body("Refresh Token inválido ou expirado");
    }
}