package com.itau.seguros.service.calculadora;

import java.math.BigDecimal;

public class CalculadoraImpostosVida implements CalculadoraImpostos {
    @Override
    public BigDecimal calcular(BigDecimal precoBase) {
        BigDecimal iof = new BigDecimal("0.01");
        BigDecimal pis = new BigDecimal("0.022");
        return precoBase.add(precoBase.multiply(iof)).add(precoBase.multiply(pis));
    }
}
