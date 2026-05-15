package com.caixaone.caixaone.dto;

import com.caixaone.caixaone.model.Cargo;
import lombok.Data;

@Data
public class RegisterDTO {
    private String nome;
    private String email;
    private String senha;
    private Cargo cargo;
}