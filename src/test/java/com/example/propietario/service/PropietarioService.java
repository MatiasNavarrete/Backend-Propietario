package com.example.propietario.service;

import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.entity.Propietario;
import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;
import com.example.propietario.mapper.PropietarioMapper;
import com.example.propietario.repository.PropietarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropietarioServiceTest {

    @Mock
    private PropietarioRepository propietarioRepository;

    @Mock
    private PropietarioMapper propietarioMapper;

    @InjectMocks
    private PropietarioServiceImpl propietarioService;

    @Test
    void cuandoGuardarPropietario_entoncesRetornaPropietarioGuardado() {
        UUID idSimulado = UUID.randomUUID();

        Propietario propietarioParaGuardar = new Propietario();
        propietarioParaGuardar.setName("Pepe");
        propietarioParaGuardar.setEmail("pepe@u.cl");

        Propietario propietarioGuardado = new Propietario();
        propietarioGuardado.setId(idSimulado);
        propietarioGuardado.setName("Pepe");
        propietarioGuardado.setEmail("pepe@u.cl");

        PropietarioResponseDto responseSimulado = new PropietarioResponseDto(
                idSimulado,
                "Pepe",
                "pepe@u.cl",
                "+569",
                "aqui",
                TipoPropietario.NATURAL,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        when(propietarioMapper.toEntity(any(PropietarioRequestDto.class))).thenReturn(propietarioParaGuardar);
        when(propietarioRepository.save(any(Propietario.class))).thenReturn(propietarioGuardado);
        when(propietarioMapper.toResponseDto(any(Propietario.class))).thenReturn(responseSimulado); // <-- EL PASO QUE FALTABA

        PropietarioRequestDto request = new PropietarioRequestDto(
                "Pepe",
                "pepe@u.cl",
                "+569",
                "Direccion",
                "S",
                "1",
                TipoPropietario.NATURAL,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        var resultado = propietarioService.register(request);


        assertNotNull(resultado);
        assertEquals("Pepe", resultado.name()); // Cambia a .getName() si no es record
        assertEquals(idSimulado, resultado.id());
    }
    @Test
    void cuandoGuardarPropietarioConEmailExistente_entoncesLanzaExcepcion() {
        String emailRepetido = "fade@u.cl";

        lenient().when(propietarioRepository.existsByEmail(emailRepetido)).thenReturn(true);

        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", emailRepetido, "+569", "Direccion", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, "image"

        );

        assertThrows(RuntimeException.class, () -> {
            propietarioService.register(request);
        });
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaPropietario() {
        UUID id = UUID.randomUUID();
        Propietario propietarioEnDb = new Propietario();
        propietarioEnDb.setId(id);
        propietarioEnDb.setName("Pepe"); // Asegúrate si es .setName o .setNombre en tu Entidad

        PropietarioResponseDto responseDto = new PropietarioResponseDto(
                id,
                "Pepe",
                "pepe@u.cl",
                "+569",
                "a",
                TipoPropietario.FUNDACION,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        when(propietarioRepository.findById(id)).thenReturn(Optional.of(propietarioEnDb));
        when(propietarioMapper.toResponseDto(propietarioEnDb)).thenReturn(responseDto);

        PropietarioResponseDto resultado = propietarioService.findById(id);

        assertNotNull(resultado);
        assertEquals("Pepe", resultado.name());
    }
    @Test
    void cuandoListarTodos_entoncesRetornaListaDePropietarios() {
        Propietario p1 = new Propietario();
        p1.setName("Fade");

        PropietarioResponseDto responseDto = new PropietarioResponseDto(
                UUID.randomUUID(),
                "Fade",
                "fade@u.cl",
                "+569",
                "venezuela",
                TipoPropietario.JURIDICO,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.SIN_MASCOTAS_PERDIDAS,
                "image"
        );

        when(propietarioRepository.findAll()).thenReturn(java.util.List.of(p1));
        when(propietarioMapper.toResponseDto(p1)).thenReturn(responseDto);

        var lista = propietarioService.findAll(); // Ajusta si tu método se llama getAll()

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Fade", lista.get(0).name());
    }
    @Test
    void cuandoEliminarPropietarioExistente_entoncesNoLanzaExcepcion() {
        UUID id = UUID.randomUUID();
        Propietario propietarioExistente = new Propietario();
        propietarioExistente.setId(id);

        when(propietarioRepository.findById(id)).thenReturn(java.util.Optional.of(propietarioExistente));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            propietarioService.delete(id);
        });

        verify(propietarioRepository, times(1)).delete(propietarioExistente);
    }
    @Test
    void cuandoEliminarPropietarioInexistente_entoncesLanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();

        when(propietarioRepository.findById(idInexistente)).thenReturn(java.util.Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> {
            propietarioService.delete(idInexistente);
        });

        assertTrue(exception.getMessage().contains("No se encontro el propietario con esta id"));
    }
}