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
            logger.error("Tentativa de salvar um produto nulo");
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }

        logger.info("Salvando novo produto: Nome={}, Categoria={}", produto.getNome(), produto.getCategoria());

        CalculadoraImpostos calculadora = calculadoraImpostosFactory.getCalculadora(produto.getCategoria());

        BigDecimal precoTarifado = calculadora.calcular(produto.getPrecoBase());
        produto.setPrecoTarifado(precoTarifado);

        Produto produtoSalvo = produtoRepository.save(produto);
        logger.info("Produto salvo com sucesso: ID={}, Nome={}, Preço Base={}, Preço Tarifado={}",
                produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getPrecoBase(), produtoSalvo.getPrecoTarifado());

        return produtoSalvo;
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        logger.info("Buscando produto com ID={}", id);
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isPresent()) {
            logger.info("Produto encontrado: ID={}, Nome={}", produto.get().getId(), produto.get().getNome());
        } else {
            logger.warn("Produto não encontrado para o ID={}", id);
        }

        return produto;
    }

    public Produto atualizarProduto(UUID id, Produto produtoAtualizado) {
        logger.info("Atualizando produto com ID={}", id);

        Optional<Produto> produtoExistente = buscarProdutoPorId(id);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();

            logger.info("Atualizando informações do produto: Nome={}, Categoria={}, Preço Base={}",
                    produtoAtualizado.getNome(), produtoAtualizado.getCategoria(), produtoAtualizado.getPrecoBase());

            produto.setNome(produtoAtualizado.getNome());
            produto.setCategoria(produtoAtualizado.getCategoria());
            produto.setPrecoBase(produtoAtualizado.getPrecoBase());

            BigDecimal precoTarifado = calculadoraImpostosFactory
                    .getCalculadora(produto.getCategoria())
                    .calcular(produto.getPrecoBase());

            produto.setPrecoTarifado(precoTarifado);

            Produto produtoAtualizadoFinal = produtoRepository.save(produto);

            logger.info("Produto atualizado com sucesso: ID={}, Nome={}, Preço Base={}, Preço Tarifado={}",
                    produtoAtualizadoFinal.getId(), produtoAtualizadoFinal.getNome(),
                    produtoAtualizadoFinal.getPrecoBase(), produtoAtualizadoFinal.getPrecoTarifado());

            return produtoAtualizadoFinal;
        }

        logger.error("Erro ao atualizar. Produto com ID={} não encontrado", id);
        throw new RuntimeException("Produto não encontrado");
    }
}
