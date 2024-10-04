package com.itau.seguros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.seguros.dto.ProdutoRequestDTO;
import com.itau.seguros.model.Produto;
import com.itau.seguros.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarProduto() throws Exception {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Seguro de Vida");
        produto.setCategoria("VIDA");
        produto.setPrecoBase(new BigDecimal("100.00"));
        produto.setPrecoTarifado(new BigDecimal("103.20"));

        Mockito.when(produtoService.salvarProduto(Mockito.any(ProdutoRequestDTO.class))).thenReturn(produto);

        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("Seguro de Vida");
        requestDTO.setCategoria("VIDA");
        requestDTO.setPrecoBase(new BigDecimal("100.00"));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Seguro de Vida")))
                .andExpect(jsonPath("$.categoria", is("VIDA")))
                .andExpect(jsonPath("$.precoTarifado", is(103.20)));
    }

    @Test
    void buscarProduto() throws Exception {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Seguro Auto");
        produto.setCategoria("AUTO");
        produto.setPrecoBase(new BigDecimal("200.00"));
        produto.setPrecoTarifado(new BigDecimal("210.50"));

        Mockito.when(produtoService.buscarProdutoPorId(Mockito.any(UUID.class))).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/api/produtos/{id}", produto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome", is("Seguro Auto")))
                .andExpect(jsonPath("$.categoria", is("AUTO")))
                .andExpect(jsonPath("$.precoTarifado", is(210.50)));
    }

    @Test
    void buscarProdutoNaoEncontrado() throws Exception {
        Mockito.when(produtoService.buscarProdutoPorId(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarProdutoDadosInvalidos() throws Exception {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("");
        requestDTO.setCategoria("VIDA");
        requestDTO.setPrecoBase(new BigDecimal("100.00"));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
