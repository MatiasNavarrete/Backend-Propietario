package com.example.propietario.dto;

import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropietarioDtoTest {

    @Test
    @DisplayName("Debería construir y leer PropietarioRequestDto")
    void testPropietarioRequestDto() {
        PropietarioRequestDto requestDto = new PropietarioRequestDto(
                "Fade",
                "fade@example.com",
                "+56912345678",
                "Direccion Ficticia",
                "Nombre Contacto",
                "+56911111111",
                TipoPropietario.NATURAL,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        assertNotNull(requestDto);
        assertEquals("Fade", requestDto.name());
        assertEquals("Nombre Contacto", requestDto.secondaryContactName());
        assertEquals("fade@example.com", requestDto.email());
        assertEquals("+56912345678", requestDto.phoneNumber());
        assertEquals("Direccion Ficticia", requestDto.address());
        assertEquals("image", requestDto.image());
        assertEquals(EstadoBusqueda.BUSCANDO, requestDto.estadoBusqueda());
    }

    @Test
    @DisplayName("Debería construir y leer PropietarioResponseDto")
    void testPropietarioResponseDto() {
        UUID id = UUID.randomUUID();
        PropietarioResponseDto responseDto = new PropietarioResponseDto(
                id,
                "Fade",
                "fade@example.com",
                "+56912345678",
                "Direccion Ficticia",
                null,
                null,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        assertNotNull(responseDto);
        assertEquals(id, responseDto.id());
        assertEquals("Fade", responseDto.name());
        assertEquals("fade@example.com", responseDto.email());
        assertEquals(EstadoBusqueda.BUSCANDO, responseDto.estadoBusqueda());
    }
}