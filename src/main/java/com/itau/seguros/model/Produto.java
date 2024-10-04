package com.itau.seguros.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "produtos")
public class Produto {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String nome;

    private String categoria;
    private BigDecimal precoBase;
    private BigDecimal precoTarifado;

    public Produto() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public BigDecimal getPrecoTarifado() {
        return precoTarifado;
    }

    public void setPrecoTarifado(BigDecimal precoTarifado) {
        this.precoTarifado = precoTarifado;
    }
}