package com.example.propietario.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EnumsTest {

    @Test
    @DisplayName("Debería cubrir los métodos implícitos de EstadoBusqueda")
    void testEstadoBusqueda() {
        EstadoBusqueda[] values = EstadoBusqueda.values();
        assertNotNull(values);
        EstadoBusqueda value = EstadoBusqueda.valueOf("BUSCANDO");
        assertEquals(EstadoBusqueda.BUSCANDO, value);
    }

    @Test
    @DisplayName("Debería cubrir los métodos implícitos de EstadoCuenta")
    void testEstadoCuenta() {
        EstadoCuenta[] values = EstadoCuenta.values();
        assertNotNull(values);
        EstadoCuenta value = EstadoCuenta.valueOf("ACTIVO");
        assertEquals(EstadoCuenta.ACTIVO, value);
    }

    @Test
    @DisplayName("Debería cubrir los métodos implícitos de TipoPropietario")
    void testTipoPropietario() {
        TipoPropietario[] values = TipoPropietario.values();
        assertNotNull(values);
        TipoPropietario value = TipoPropietario.valueOf("NATURAL");
        assertEquals(TipoPropietario.NATURAL, value);
    }
}