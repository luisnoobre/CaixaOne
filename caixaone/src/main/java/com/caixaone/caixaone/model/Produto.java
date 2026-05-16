package com.caixaone.caixaone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String codigo;
    private String codigoBarras;
    private String descricao;

    @Column(name = "preco_custo", precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(nullable = false)
    private Integer estoque;

    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;

    @Column(name = "desconto")
    private Double desconto;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}