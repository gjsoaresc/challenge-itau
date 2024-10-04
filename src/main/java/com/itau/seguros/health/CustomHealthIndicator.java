package com.itau.seguros.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean customServiceUp = checkCustomComponentHealth();

        if (customServiceUp) {
            return Health.up().withDetail("Custom Component", "Funcionando corretamente").build();
        } else {
            return Health.down().withDetail("Custom Component", "Fora do ar").build();
        }
    }

    private boolean checkCustomComponentHealth() {
        return true;
    }
}
