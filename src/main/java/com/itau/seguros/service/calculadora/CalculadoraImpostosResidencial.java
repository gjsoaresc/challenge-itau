package com.itau.seguros.service.calculadora;

import java.math.BigDecimal;

public class CalculadoraImpostosResidencial implements CalculadoraImpostos {
    @Override
    public BigDecimal calcular(BigDecimal precoBase) {
        BigDecimal iof = new BigDecimal("0.04");
        BigDecimal cofins = new BigDecimal("0.03");

        return precoBase.add(precoBase.multiply(iof)).add(precoBase.multiply(cofins));
    }
}
