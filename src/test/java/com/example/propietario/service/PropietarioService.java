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
        // 1. Arrange
        UUID idSimulado = UUID.randomUUID();

        // Entidad intermedia
        Propietario propietarioParaGuardar = new Propietario();
        propietarioParaGuardar.setName("Pepe");
        propietarioParaGuardar.setEmail("pepe@u.cl");

        // Entidad que sale de la DB
        Propietario propietarioGuardado = new Propietario();
        propietarioGuardado.setId(idSimulado);
        propietarioGuardado.setName("Pepe");
        propietarioGuardado.setEmail("pepe@u.cl");

        // El DTO que el servicio debe retornar al final (Ajusta los campos según tu record)
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

        // Simulamos los 3 pasos del ServiceImpl
        when(propietarioMapper.toEntity(any(PropietarioRequestDto.class))).thenReturn(propietarioParaGuardar);
        when(propietarioRepository.save(any(Propietario.class))).thenReturn(propietarioGuardado);
        when(propietarioMapper.toResponseDto(any(Propietario.class))).thenReturn(responseSimulado); // <-- EL PASO QUE FALTABA

        // Datos de entrada
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

        // 2. Act
        var resultado = propietarioService.register(request);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals("Pepe", resultado.name()); // Cambia a .getName() si no es record
        assertEquals(idSimulado, resultado.id());
    }
    @Test
    void cuandoGuardarPropietarioConEmailExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        String emailRepetido = "fade@u.cl";

        // Usamos LENIENT para que Mockito no se queje de los otros mocks que no usamos aquí
        lenient().when(propietarioRepository.existsByEmail(emailRepetido)).thenReturn(true);

        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", emailRepetido, "+569", "Direccion", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, "image"

        );

        // 2. Act & 3. Assert
        assertThrows(RuntimeException.class, () -> {
            propietarioService.register(request);
        });
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaPropietario() {
        // 1. Arrange
        UUID id = UUID.randomUUID();
        Propietario propietarioEnDb = new Propietario();
        propietarioEnDb.setId(id);
        propietarioEnDb.setName("Pepe"); // Asegúrate si es .setName o .setNombre en tu Entidad

        // Aquí está el truco: mira bien los nombres de los campos de tu record
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

        // 2. Act
        PropietarioResponseDto resultado = propietarioService.findById(id);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals("Pepe", resultado.name());
    }
    @Test
    void cuandoListarTodos_entoncesRetornaListaDePropietarios() {
        // 1. Arrange
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

        // Simulamos que el repo devuelve una lista con un elemento
        when(propietarioRepository.findAll()).thenReturn(java.util.List.of(p1));
        // Simulamos que el mapper convierte ese elemento
        when(propietarioMapper.toResponseDto(p1)).thenReturn(responseDto);

        // 2. Act
        var lista = propietarioService.findAll(); // Ajusta si tu método se llama getAll()

        // 3. Assert
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Fade", lista.get(0).name());
    }
    @Test
    void cuandoEliminarPropietarioExistente_entoncesNoLanzaExcepcion() {
        // 1. Arrange
        UUID id = UUID.randomUUID();
        Propietario propietarioExistente = new Propietario();
        propietarioExistente.setId(id);

        // MOCK CLAVE: Simulamos que el repositorio SI encuentra al propietario
        when(propietarioRepository.findById(id)).thenReturn(java.util.Optional.of(propietarioExistente));

        // 2. Act & 3. Assert
        // Ahora findById no lanzará el RuntimeException y llegará al delete
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            propietarioService.delete(id);
        });

        // Verificación extra: Aseguramos que se llamó al método delete del repo con el objeto correcto
        verify(propietarioRepository, times(1)).delete(propietarioExistente);
    }
    @Test
    void cuandoEliminarPropietarioInexistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        UUID idInexistente = UUID.randomUUID();

        // Simulamos que el repo devuelve un Optional vacío
        when(propietarioRepository.findById(idInexistente)).thenReturn(java.util.Optional.empty());

        // 2. Act & 3. Assert
        var exception = assertThrows(RuntimeException.class, () -> {
            propietarioService.delete(idInexistente);
        });

        // Verificamos que el mensaje sea el que tú escribiste
        assertTrue(exception.getMessage().contains("No se encontro el propietario con esta id"));
    }
}