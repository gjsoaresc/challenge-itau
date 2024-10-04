package com.itau.seguros.service.calculadora;

import java.math.BigDecimal;

public interface CalculadoraImpostos {
    BigDecimal calcular(BigDecimal precoBase);
}
