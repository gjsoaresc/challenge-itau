package com.itau.seguros.service;

import com.itau.seguros.dto.ProdutoRequestDTO;
import com.itau.seguros.model.Produto;
import com.itau.seguros.repository.ProdutoRepository;
import com.itau.seguros.service.calculadora.CalculadoraImpostos;
import com.itau.seguros.service.calculadora.CalculadoraImpostosFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CalculadoraImpostosFactory calculadoraImpostosFactory;

    @Mock
    private CalculadoraImpostos calculadoraImpostos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void salvarProduto() {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setNome("Seguro Residencial");
        produtoRequestDTO.setCategoria("RESIDENCIAL");
        produtoRequestDTO.setPrecoBase(new BigDecimal("300.00"));

        Produto produtoEsperado = new Produto();
        produtoEsperado.setNome("Seguro Residencial");
        produtoEsperado.setCategoria("RESIDENCIAL");
        produtoEsperado.setPrecoBase(new BigDecimal("300.00"));
        produtoEsperado.setPrecoTarifado(new BigDecimal("321.00"));

        when(calculadoraImpostosFactory.getCalculadora("RESIDENCIAL")).thenReturn(calculadoraImpostos);
        when(calculadoraImpostos.calcular(produtoRequestDTO.getPrecoBase())).thenReturn(new BigDecimal("321.00"));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoEsperado);

        Produto produtoSalvo = produtoService.salvarProduto(produtoRequestDTO);

        assertNotNull(produtoSalvo);
        assertEquals(new BigDecimal("321.00"), produtoSalvo.getPrecoTarifado());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void buscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        when(produtoRepository.findById(any(UUID.class))).thenReturn(Optional.of(produto));

        Optional<Produto> produtoBuscado = produtoService.buscarProdutoPorId(UUID.randomUUID());

        assertTrue(produtoBuscado.isPresent());
        verify(produtoRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void buscarProdutoPorIdNaoEncontrado() {
        when(produtoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<Produto> produtoBuscado = produtoService.buscarProdutoPorId(UUID.randomUUID());

        assertFalse(produtoBuscado.isPresent());
        verify(produtoRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void atualizarProduto() {
        UUID id = UUID.randomUUID();

        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setNome("Seguro Vida");
        produtoExistente.setCategoria("PATRIMONIAL");
        produtoExistente.setPrecoBase(new BigDecimal("400.00"));

        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setNome("Seguro Patrimonial Atualizado");
        produtoRequestDTO.setCategoria("PATRIMONIAL");
        produtoRequestDTO.setPrecoBase(new BigDecimal("500.00"));

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Seguro Patrimonial Atualizado");
        produtoAtualizado.setCategoria("PATRIMONIAL");
        produtoAtualizado.setPrecoBase(new BigDecimal("500.00"));
        produtoAtualizado.setPrecoTarifado(new BigDecimal("540.00"));

        when(calculadoraImpostosFactory.getCalculadora("PATRIMONIAL")).thenReturn(calculadoraImpostos);
        when(calculadoraImpostos.calcular(produtoRequestDTO.getPrecoBase())).thenReturn(new BigDecimal("540.00"));
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        Produto resultado = produtoService.atualizarProduto(id, produtoRequestDTO);

        assertEquals("Seguro Patrimonial Atualizado", resultado.getNome());
        assertEquals(new BigDecimal("540.00"), resultado.getPrecoTarifado());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }
}
