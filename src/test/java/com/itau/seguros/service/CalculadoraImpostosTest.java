package com.itau.seguros.service;

import com.itau.seguros.service.calculadora.CalculadoraImpostosAuto;
import com.itau.seguros.service.calculadora.CalculadoraImpostosPatrimonial;
import com.itau.seguros.service.calculadora.CalculadoraImpostosResidencial;
import com.itau.seguros.service.calculadora.CalculadoraImpostosVida;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraImpostosTest {

    @Test
    void calcularImpostoVida() {
        CalculadoraImpostosVida calculadora = new CalculadoraImpostosVida();
        BigDecimal precoBase = new BigDecimal("100.00");
        BigDecimal precoTarifado = calculadora.calcular(precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("103.20"), precoTarifado);
    }

    @Test
    void calcularImpostoAuto() {
        CalculadoraImpostosAuto calculadora = new CalculadoraImpostosAuto();
        BigDecimal precoBase = new BigDecimal("200.00");
        BigDecimal precoTarifado = calculadora.calcular(precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("221.00"), precoTarifado);
    }

    @Test
    void calcularImpostoPatrimonial() {
        CalculadoraImpostosPatrimonial calculadora = new CalculadoraImpostosPatrimonial();
        BigDecimal precoBase = new BigDecimal("500.00");
        BigDecimal precoTarifado = calculadora.calcular(precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("540.00"), precoTarifado);
    }

    @Test
    void calcularImpostoResidencial() {
        CalculadoraImpostosResidencial calculadora = new CalculadoraImpostosResidencial();
        BigDecimal precoBase = new BigDecimal("300.00");
        BigDecimal precoTarifado = calculadora.calcular(precoBase).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("321.00"), precoTarifado);
    }
}
