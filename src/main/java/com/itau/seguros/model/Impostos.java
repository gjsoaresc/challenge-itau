package com.itau.seguros.model;

import java.math.BigDecimal;

public record Impostos(BigDecimal iof, BigDecimal pis, BigDecimal cofins) {
}