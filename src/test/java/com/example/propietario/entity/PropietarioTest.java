package com.example.propietario.entity;

import com.example.propietario.enums.EstadoBusqueda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropietarioTest {

    @Test
    @DisplayName("Debería instanciar Propietario y utilizar todos sus getters y setters de Lombok")
    void testPropietarioGettersAndSetters() {
        Propietario propietario = new Propietario();
        UUID id = UUID.randomUUID();

        propietario.setId(id);
        propietario.setName("Fade");
        propietario.setEmail("fade@example.com");
        propietario.setPhoneNumber("+56912345678");
        propietario.setAddress("Direccion Ficticia");
        propietario.setImage("data:image/jpeg;base64,mock...");
        propietario.setEstadoBusqueda(EstadoBusqueda.BUSCANDO);

        assertNotNull(propietario);
        assertEquals(id, propietario.getId());
        assertEquals("Fade", propietario.getName());
        assertEquals("fade@example.com", propietario.getEmail());
        assertEquals("+56912345678", propietario.getPhoneNumber());
        assertEquals("Direccion Ficticia", propietario.getAddress());
        assertEquals("data:image/jpeg;base64,mock...", propietario.getImage());
        assertEquals(EstadoBusqueda.BUSCANDO, propietario.getEstadoBusqueda());
    }
}