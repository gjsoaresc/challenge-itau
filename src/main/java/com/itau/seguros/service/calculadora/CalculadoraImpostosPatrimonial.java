package com.itau.seguros.service.calculadora;

import java.math.BigDecimal;

public class CalculadoraImpostosPatrimonial implements CalculadoraImpostos {
    @Override
    public BigDecimal calcular(BigDecimal precoBase) {
        BigDecimal iof = new BigDecimal("0.05");
        BigDecimal pis = new BigDecimal("0.03");

        return precoBase.add(precoBase.multiply(iof))
                .add(precoBase.multiply(pis));
    }
}
