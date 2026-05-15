package com.caixaone.caixaone.service;

import com.caixaone.caixaone.dto.LoginDTO;
import com.caixaone.caixaone.dto.RegisterDTO;
import com.caixaone.caixaone.model.Usuario;
import com.caixaone.caixaone.repository.UsuarioRepository;
import com.caixaone.caixaone.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        return jwtUtil.gerarToken(auth.getName());
    }

    public Usuario register(RegisterDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado!");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setCargo(dto.getCargo());
        return usuarioRepository.save(usuario);
    }
}