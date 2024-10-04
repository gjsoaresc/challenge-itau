package com.itau.seguros.service;

import com.itau.seguros.dto.ProdutoRequestDTO;
import com.itau.seguros.model.Produto;
import com.itau.seguros.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto salvarProduto(ProdutoRequestDTO produtoRequestDTO) {
        verificarDuplicidade(produtoRequestDTO.getNome());

        Produto produto = new Produto();
        aplicarValoresECalcularPreco(produto, produtoRequestDTO);

        return produtoRepository.save(produto);
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        return produtoRepository.findById(id);
    }

    public Produto atualizarProduto(UUID id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoExistente = buscarProdutoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID " + id + " não encontrado"));

        verificarDuplicidade(produtoRequestDTO.getNome(), id);

        aplicarValoresECalcularPreco(produtoExistente, produtoRequestDTO);

        return produtoRepository.save(produtoExistente);
    }

    private void verificarDuplicidade(String nome) {
        produtoRepository.findByNome(nome)
                .ifPresent(produto -> {
                    logger.warn("Tentativa de criar produto duplicado: Nome={}", nome);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto com o nome '" + nome + "' já existe.");
                });
    }

    private void verificarDuplicidade(String nome, UUID id) {
        produtoRepository.findByNome(nome)
                .filter(produto -> !produto.getId().equals(id))
                .ifPresent(produto -> {
                    logger.warn("Produto com o nome '{}' já existe.", nome);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto com o nome '" + nome + "' já existe.");
                });
    }

    private void aplicarValoresECalcularPreco(Produto produto, ProdutoRequestDTO produtoRequestDTO) {
        produto.setNome(produtoRequestDTO.getNome());
        produto.setCategoria(produtoRequestDTO.getCategoria());
        produto.setPrecoBase(produtoRequestDTO.getPrecoBase());

        BigDecimal precoTarifado = CalculadoraImpostos.getCalculadora(produto.getCategoria(), produto.getPrecoBase());

        produto.setPrecoTarifado(precoTarifado);
    }
}
