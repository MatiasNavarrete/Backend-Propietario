package com.example.propietario.mapper;

import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.entity.Propietario;
import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PropietarioMapperTest {

    private final PropietarioMapper mapper = Mappers.getMapper(PropietarioMapper.class);

    @Test
    @DisplayName("Debería mapear correctamente de PropietarioRequestDto a la Entidad Propietario")
    void toEntity_ShouldMapCorrectly() {
       PropietarioRequestDto requestDto = new PropietarioRequestDto(
                "Fade",
                "fade@example.com",
                "+56912345678",
                "Direccion Ficticia",
                "data:image/jpeg;base64,mock...",
                null,
                TipoPropietario.NATURAL,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        Propietario entity = mapper.toEntity(requestDto);

        assertNotNull(entity);
        assertEquals("Fade", entity.getName());
        assertEquals("fade@example.com", entity.getEmail());
        assertEquals("+56912345678", entity.getPhoneNumber());
        assertEquals("Direccion Ficticia", entity.getAddress());
        assertEquals(EstadoBusqueda.BUSCANDO, entity.getEstadoBusqueda());
    }

    @Test
    @DisplayName("Debería mapear correctamente de la Entidad Propietario a PropietarioResponseDto")
    void toDto_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        Propietario entity = new Propietario();
        entity.setId(id);
        entity.setName("Fade");
        entity.setEmail("fade@example.com");
        entity.setPhoneNumber("+56912345678");
        entity.setAddress("Direccion Ficticia");
        entity.setEstadoBusqueda(EstadoBusqueda.BUSCANDO);
        entity.setImage("data:image/jpeg;base64,mock...");

        PropietarioResponseDto responseDto = mapper.toResponseDto(entity);

        assertNotNull(responseDto);
        assertEquals(id, responseDto.id());
        assertEquals("Fade", responseDto.name());
        assertEquals("fade@example.com", responseDto.email());
        assertEquals(EstadoBusqueda.BUSCANDO, responseDto.estadoBusqueda());
    }
    @Test
    @DisplayName("Debería retornar null cuando el RequestDto a mapear es nulo")
    void toEntity_Null_ShouldReturnNull() {
        Propietario entity = mapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    @DisplayName("Debería retornar null cuando la Entidad a mapear es nula")
    void toDto_Null_ShouldReturnNull() {
        PropietarioResponseDto responseDto = mapper.toResponseDto(null);

        assertNull(responseDto);
    }
}