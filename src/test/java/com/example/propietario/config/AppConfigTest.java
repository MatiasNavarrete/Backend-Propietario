package com.example.propietario.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AppConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private WebMvcConfigurer webConfig;

    @Autowired
    private CircuitBreakerRegistry resilienceConfig;

    @Test
    @DisplayName("Debería levantar el contexto y ejecutar todas las lambdas de configuración")
    void testAllConfigsLoadCorrectly() {
        assertNotNull(securityFilterChain, "El filtro de seguridad no debe ser nulo");
        assertNotNull(userDetailsService, "El usuario en memoria no debe ser nulo");
        assertNotNull(webConfig, "La configuración CORS no debe ser nula");
        assertNotNull(resilienceConfig, "El CircuitBreaker no debe ser nulo");
    }
}