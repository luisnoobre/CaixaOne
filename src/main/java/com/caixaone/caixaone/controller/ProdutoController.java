package com.caixaone.caixaone.controller;

import com.caixaone.caixaone.model.Produto;
import com.caixaone.caixaone.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<Produto> buscarPorNome(@RequestParam String nome) {
        return produtoService.buscarPorNome(nome);
    }

    @GetMapping("/estoque-baixo")
    public List<Produto> estoqueBaixo(@RequestParam(defaultValue = "5") Integer minimo) {
        return produtoService.listarEstoqueBaixo(minimo);
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.salvar(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.atualizar(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.ok("Produto deletado!");
    }
}