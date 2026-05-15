package com.caixaone.caixaone.service;

import com.caixaone.caixaone.model.Produto;
import com.caixaone.caixaone.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Produto> listarEstoqueBaixo(Integer minimo) {
        return produtoRepository.findByEstoqueLessThanEqual(minimo);
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produto) {
        Produto existente = buscarPorId(id);
        existente.setNome(produto.getNome());
        existente.setCodigo(produto.getCodigo());
        existente.setCodigoBarras(produto.getCodigoBarras());
        existente.setDescricao(produto.getDescricao());
        existente.setPrecoCusto(produto.getPrecoCusto());
        existente.setPrecoVenda(produto.getPrecoVenda());
        existente.setEstoque(produto.getEstoque());
        existente.setEstoqueMinimo(produto.getEstoqueMinimo());
        existente.setCategoria(produto.getCategoria());
        return produtoRepository.save(existente);
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}