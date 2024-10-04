package com.itau.seguros.service;

import com.itau.seguros.model.Produto;
import com.itau.seguros.repository.ProdutoRepository;
import com.itau.seguros.service.calculadora.CalculadoraImpostos;
import com.itau.seguros.service.calculadora.CalculadoraImpostosFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {
    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CalculadoraImpostosFactory calculadoraImpostosFactory;

    public Produto salvarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }

        if (produtoRepository.findByNome(produto.getNome()).isPresent()) {
            logger.warn("Tentativa de criar produto duplicado: Nome={}", produto.getNome());
            throw new IllegalArgumentException("Produto com o nome '" + produto.getNome() + "' já existe.");
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

            Optional<Produto> produtoComMesmoNome = produtoRepository.findByNome(produtoAtualizado.getNome());
            if (produtoComMesmoNome.isPresent() && !produtoComMesmoNome.get().getId().equals(id)) {
                logger.warn("Produto com o nome '{}' já existe", produtoAtualizado.getNome());
                throw new IllegalArgumentException("Produto com o nome '" + produtoAtualizado.getNome() + "' já existe.");
            }

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
