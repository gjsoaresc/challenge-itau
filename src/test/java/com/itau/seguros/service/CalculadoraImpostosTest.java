package com.itau.seguros.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraImpostosTest {

    @Test
    void calcularImpostoVida() {
        BigDecimal precoBase = new BigDecimal("100.00");
        BigDecimal precoTarifado = CalculadoraImpostos.getCalculadora("VIDA", precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("103.20"), precoTarifado);
    }

    @Test
    void calcularImpostoAuto() {
        BigDecimal precoBase = new BigDecimal("200.00");
        BigDecimal precoTarifado = CalculadoraImpostos.getCalculadora("AUTO", precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("221.00"), precoTarifado);
    }

    @Test
    void calcularImpostoPatrimonial() {
        BigDecimal precoBase = new BigDecimal("500.00");
        BigDecimal precoTarifado = CalculadoraImpostos.getCalculadora("PATRIMONIAL", precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("540.00"), precoTarifado);
    }

    @Test
    void calcularImpostoResidencial() {
        BigDecimal precoBase = new BigDecimal("300.00");
        BigDecimal precoTarifado = CalculadoraImpostos.getCalculadora("RESIDENCIAL", precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("321.00"), precoTarifado);
    }
}
