package com.caixaone.caixaone.service;

import com.caixaone.caixaone.model.*;
import com.caixaone.caixaone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Venda realizarVenda(Long usuarioId, FormaPagamento formaPagamento, List<ItemVenda> itens) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento(formaPagamento);
        venda.setUsuario(usuario);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoRepository.save(produto);

            BigDecimal subtotal = produto.getPrecoVenda()
                .multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setSubtotal(subtotal);
            item.setVenda(venda);
            item.setProduto(produto);

            total = total.add(subtotal);
        }

        venda.setValorTotal(total);
        venda.setItens(itens);

        return vendaRepository.save(venda);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada!"));
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }
}