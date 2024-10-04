package com.itau.seguros.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Representa o produto de seguro")
public class ProdutoResponseDTO {
    @Schema(description = "ID do produto", example = "670b628d-a5a8-486f-80ed-aea8ac91fd81")
    private UUID id;

    @Schema(description = "Nome do produto", example = "Seguro de Vida")
    private String nome;

    @Schema(description = "Categoria do produto", example = "VIDA")
    private String categoria;

    @Schema(description = "Preço base do produto", example = "100.00")
    private BigDecimal precoBase;

    @Schema(description = "Preço tarifado após aplicação dos impostos", example = "103.20")
    private BigDecimal precoTarifado;

    public ProdutoResponseDTO(UUID id, String nome, String categoria, BigDecimal precoBase, BigDecimal precoTarifado) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.precoBase = precoBase;
        this.precoTarifado = precoTarifado;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public BigDecimal getPrecoTarifado() {
        return precoTarifado;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public void setPrecoTarifado(BigDecimal precoTarifado) {
        this.precoTarifado = precoTarifado;
    }
}