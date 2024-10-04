package com.itau.seguros.service.calculadora;

import java.math.BigDecimal;

public class CalculadoraImpostosAuto implements CalculadoraImpostos {
    @Override
    public BigDecimal calcular(BigDecimal precoBase) {
        BigDecimal iof = new BigDecimal("0.055");
        BigDecimal pis = new BigDecimal("0.04");
        BigDecimal cofins = new BigDecimal("0.01");
        return precoBase.add(precoBase.multiply(iof))
                .add(precoBase.multiply(pis))
                .add(precoBase.multiply(cofins));
    }
}
