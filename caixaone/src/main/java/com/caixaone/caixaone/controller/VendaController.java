package com.caixaone.caixaone.controller;

import com.caixaone.caixaone.model.*;
import com.caixaone.caixaone.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<Venda> realizarVenda(@RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        FormaPagamento forma = FormaPagamento.valueOf(body.get("formaPagamento").toString());

        List<Map<String, Object>> itensRaw = (List<Map<String, Object>>) body.get("itens");
        List<ItemVenda> itens = itensRaw.stream().map(i -> {
            ItemVenda item = new ItemVenda();
            Produto p = new Produto();
            p.setId(Long.valueOf(i.get("produtoId").toString()));
            item.setProduto(p);
            item.setQuantidade(Integer.valueOf(i.get("quantidade").toString()));
            return item;
        }).toList();

        return ResponseEntity.ok(vendaService.realizarVenda(usuarioId, forma, itens));
    }

    @GetMapping
    public List<Venda> listar() {
        return vendaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/periodo")
    public List<Venda> buscarPorPeriodo(
            @RequestParam String inicio,
            @RequestParam String fim) {
        return vendaService.buscarPorPeriodo(
            LocalDateTime.parse(inicio),
            LocalDateTime.parse(fim));
    }
}