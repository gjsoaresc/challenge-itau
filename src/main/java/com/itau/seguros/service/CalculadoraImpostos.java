package com.itau.seguros.service;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.Map;

public class CalculadoraImpostos {

    private static final Map<String, Function<BigDecimal, BigDecimal>> CALCULADORAS = Map.of(
            "AUTO", CalculadoraImpostos::auto,
            "VIDA", CalculadoraImpostos::vida,
            "RESIDENCIAL", CalculadoraImpostos::residencial,
            "PATRIMONIAL", CalculadoraImpostos::patrimonial
    );

    public static BigDecimal getCalculadora(String categoria, BigDecimal precoBase) {
        return CALCULADORAS.getOrDefault(categoria.toUpperCase(),
                        pb -> { throw new IllegalArgumentException("Categoria inválida: " + categoria); })
                .apply(precoBase);
    }

    private static BigDecimal auto(BigDecimal precoBase) {
        return precoBase.add(precoBase.multiply(new BigDecimal("0.055")))
                .add(precoBase.multiply(new BigDecimal("0.04")))
                .add(precoBase.multiply(new BigDecimal("0.01")));
    }

    private static BigDecimal vida(BigDecimal precoBase) {
        return precoBase.add(precoBase.multiply(new BigDecimal("0.01")))
                .add(precoBase.multiply(new BigDecimal("0.022")));
    }

    private static BigDecimal patrimonial(BigDecimal precoBase) {
        return precoBase.add(precoBase.multiply(new BigDecimal("0.05")))
                .add(precoBase.multiply(new BigDecimal("0.03")));
    }

    private static BigDecimal residencial(BigDecimal precoBase) {
        return precoBase.add(precoBase.multiply(new BigDecimal("0.04")))
                .add(precoBase.multiply(new BigDecimal("0.03")));
    }
}
