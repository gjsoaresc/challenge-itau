package com.itau.seguros.service;

import com.itau.seguros.model.Produto;
import com.itau.seguros.repository.ProdutoRepository;
import com.itau.seguros.service.calculadora.CalculadoraImpostos;
import com.itau.seguros.service.calculadora.CalculadoraImpostosFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CalculadoraImpostosFactory calculadoraImpostosFactory;

    public Produto salvarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }

        CalculadoraImpostos calculadora = calculadoraImpostosFactory.getCalculadora(produto.getCategoria());

        BigDecimal precoTarifado = calculadora.calcular(produto.getPrecoBase());
        produto.setPrecoTarifado(precoTarifado);
        
        return produtoRepository.save(produto);
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        return produtoRepository.findById(id);
    }

    public Produto atualizarProduto(UUID id, Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = buscarProdutoPorId(id);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setCategoria(produtoAtualizado.getCategoria());
            produto.setPrecoBase(produtoAtualizado.getPrecoBase());
            BigDecimal precoTarifado = calculadoraImpostosFactory
                    .getCalculadora(produto.getCategoria())
                    .calcular(produto.getPrecoBase());
            produto.setPrecoTarifado(precoTarifado);
            return produtoRepository.save(produto);
        }
        throw new RuntimeException("Produto não encontrado");
    }
}