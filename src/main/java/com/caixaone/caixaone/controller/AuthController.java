package com.caixaone.caixaone.controller;

import com.caixaone.caixaone.dto.LoginDTO;
import com.caixaone.caixaone.dto.RegisterDTO;
import com.caixaone.caixaone.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterDTO dto) {
        var usuario = authService.register(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Usuário criado!");
        response.put("id", usuario.getId());
        return ResponseEntity.ok(response);
    }
}