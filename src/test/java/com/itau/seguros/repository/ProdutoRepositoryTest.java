package com.itau.seguros.repository;

import com.itau.seguros.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Seguro Patrimonial");
        produto.setCategoria("PATRIMONIAL");
        produto.setPrecoBase(new BigDecimal("300.00"));
        produto.setPrecoTarifado(new BigDecimal("315.00"));
    }

    @Test
    void deveSalvarProduto() {
        Produto produtoSalvo = produtoRepository.save(produto);

        assertThat(produtoSalvo).isNotNull();
        assertThat(produtoSalvo.getId()).isNotNull();
        assertThat(produtoSalvo.getNome()).isEqualTo("Seguro Patrimonial");
        assertThat(produtoSalvo.getPrecoTarifado()).isEqualTo(new BigDecimal("315.00"));
    }

    @Test
    void deveBuscarProdutoPorId() {
        Produto produtoSalvo = produtoRepository.save(produto);

        Optional<Produto> produtoEncontrado = produtoRepository.findById(produtoSalvo.getId());

        assertThat(produtoEncontrado).isPresent();
        assertThat(produtoEncontrado.get().getId()).isEqualTo(produtoSalvo.getId());
        assertThat(produtoEncontrado.get().getNome()).isEqualTo("Seguro Patrimonial");
        assertThat(produtoEncontrado.get().getPrecoTarifado()).isEqualTo(new BigDecimal("315.00"));
    }

    @Test
    void deveAtualizarProduto() {
        Produto produtoSalvo = produtoRepository.save(produto);

        produtoSalvo.setNome("Seguro Patrimonial Atualizado");
        produtoSalvo.setPrecoTarifado(new BigDecimal("325.00"));

        Produto produtoAtualizado = produtoRepository.save(produtoSalvo);

        Optional<Produto> produtoEncontrado = produtoRepository.findById(produtoAtualizado.getId());
        assertThat(produtoEncontrado).isPresent();
        assertThat(produtoEncontrado.get().getNome()).isEqualTo("Seguro Patrimonial Atualizado");
        assertThat(produtoEncontrado.get().getPrecoTarifado()).isEqualTo(new BigDecimal("325.00"));
    }

    @Test
    void deveDeletarProduto() {
        Produto produtoSalvo = produtoRepository.save(produto);

        produtoRepository.deleteById(produtoSalvo.getId());

        Optional<Produto> produtoDeletado = produtoRepository.findById(produtoSalvo.getId());
        assertThat(produtoDeletado).isNotPresent();
    }
}
