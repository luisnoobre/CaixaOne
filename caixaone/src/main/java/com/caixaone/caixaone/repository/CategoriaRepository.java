package com.caixaone.caixaone.repository;

import com.caixaone.caixaone.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}