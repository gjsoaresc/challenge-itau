package com.itau.seguros.service.calculadora;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CalculadoraImpostosFactory {
    private final Map<String, CalculadoraImpostos> calculadoras = new HashMap<>();

    public CalculadoraImpostosFactory() {
        calculadoras.put("VIDA", new CalculadoraImpostosVida());
        calculadoras.put("AUTO", new CalculadoraImpostosAuto());
        calculadoras.put("RESIDENCIAL", new CalculadoraImpostosResidencial());
        calculadoras.put("PATRIMONIAL", new CalculadoraImpostosPatrimonial());
    }

    public CalculadoraImpostos getCalculadora(String categoria) {
        CalculadoraImpostos calculadora = calculadoras.get(categoria);
        if (calculadora == null) {
            throw new IllegalArgumentException("Categoria inválida: " + categoria);
        }
        return calculadora;
    }
}
