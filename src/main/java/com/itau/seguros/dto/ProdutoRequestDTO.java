package com.itau.seguros.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoRequestDTO {
    @NotBlank(message = "O nome do produto não pode estar vazio")
    private String nome;

    @NotBlank(message = "A categoria do produto não pode estar vazia")
    private String categoria;

    @NotNull(message = "O preço base do produto não pode ser nulo")
    private BigDecimal precoBase;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }
}
