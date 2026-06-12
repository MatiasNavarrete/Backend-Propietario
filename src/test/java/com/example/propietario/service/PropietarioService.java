package com.example.propietario.service;

import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.entity.Propietario;
import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;
import com.example.propietario.mapper.PropietarioMapper;
import com.example.propietario.repository.PropietarioRepository;
import org.junit.jupiter.api.DisplayName;
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

        var lista = propietarioService.findAll();

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
    @Test
    @DisplayName("Debería lanzar excepción al registrar si el email ya existe usando findByEmail")
    void register_WhenEmailExists_ShouldThrowException() {
        String emailRepetido = "fade@u.cl";
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", emailRepetido, "+569", "Direccion", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, "image"
        );

        Propietario propietarioExistente = new Propietario();
        when(propietarioRepository.findByEmail(emailRepetido)).thenReturn(Optional.of(propietarioExistente));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> propietarioService.register(request));
        assertTrue(ex.getMessage().contains("ya está registrado"));
    }

    @Test
    @DisplayName("Debería asignar valores por defecto en register si llegan nulos")
    void register_WhenEstadosSonNulos_ShouldAssignDefaultValues() {
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Pepe", "nuevo@u.cl", "+569", "Direccion", "S", "1",
                TipoPropietario.NATURAL, null, null, "image" // Estados nulos
        );

        Propietario entidadMapeada = new Propietario();

        when(propietarioRepository.findByEmail("nuevo@u.cl")).thenReturn(Optional.empty());
        when(propietarioMapper.toEntity(any(PropietarioRequestDto.class))).thenReturn(entidadMapeada);
        when(propietarioRepository.save(any(Propietario.class))).thenReturn(entidadMapeada);

        PropietarioResponseDto responseSimulado = new PropietarioResponseDto(
                UUID.randomUUID(), "Pepe", "nuevo@u.cl", "+569", "Direccion",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, "image"
        );
        when(propietarioMapper.toResponseDto(any(Propietario.class))).thenReturn(responseSimulado);

        propietarioService.register(request);

        assertEquals(EstadoCuenta.ACTIVO, entidadMapeada.getEstadoCuenta());
        assertEquals(EstadoBusqueda.BUSCANDO, entidadMapeada.getEstadoBusqueda());
    }

    @Test
    @DisplayName("Debería guardar propietario y procesar imagen correctamente usando el método save()")
    void save_WithImage_ShouldProcessAndSave() throws Exception {
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", "fade@u.cl", "+569", "Dir", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, null
        );

        Propietario entidad = new Propietario();
        when(propietarioMapper.toEntity(request)).thenReturn(entidad);
        when(propietarioRepository.save(entidad)).thenReturn(entidad);

        PropietarioResponseDto responseMock = new PropietarioResponseDto(
                UUID.randomUUID(), "Fade", "fade@u.cl", "+569", "Dir",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, "data:image/jpeg;base64,mock..."
        );
        when(propietarioMapper.toResponseDto(entidad)).thenReturn(responseMock);

        org.springframework.web.multipart.MultipartFile imageMock = mock(org.springframework.web.multipart.MultipartFile.class);
        when(imageMock.isEmpty()).thenReturn(false);
        when(imageMock.getBytes()).thenReturn("fake-image-bytes".getBytes());
        when(imageMock.getContentType()).thenReturn("image/jpeg");

        PropietarioResponseDto resultado = propietarioService.save(request, imageMock);

        assertNotNull(resultado);
        verify(propietarioRepository, times(1)).save(entidad);
        assertTrue(entidad.getImage().startsWith("data:image/jpeg;base64,"));
    }
    @Test
    @DisplayName("Debería guardar propietario ignorando la imagen cuando viene nula")
    void save_WithNullImage_ShouldSaveWithoutProcessingImage() {
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", "fade@u.cl", "+569", "Dir", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, null
        );
        Propietario entidad = new Propietario();

        when(propietarioMapper.toEntity(request)).thenReturn(entidad);
        when(propietarioRepository.save(entidad)).thenReturn(entidad);

        propietarioService.save(request, null);

        verify(propietarioRepository, times(1)).save(entidad);
    }

    @Test
    @DisplayName("Debería guardar propietario ignorando la imagen cuando viene vacía")
    void save_WithEmptyImage_ShouldSaveWithoutProcessingImage() {
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", "fade@u.cl", "+569", "Dir", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, null
        );
        Propietario entidad = new Propietario();

        when(propietarioMapper.toEntity(request)).thenReturn(entidad);
        when(propietarioRepository.save(entidad)).thenReturn(entidad);

        org.springframework.web.multipart.MultipartFile imageMock = mock(org.springframework.web.multipart.MultipartFile.class);
        when(imageMock.isEmpty()).thenReturn(true);

        propietarioService.save(request, imageMock);

        verify(propietarioRepository, times(1)).save(entidad);
    }

    @Test
    @DisplayName("Debería atrapar IOException si la imagen falla y continuar el guardado")
    void save_WithImageThrowsIOException_ShouldCatchExceptionAndSave() throws Exception {
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", "fade@u.cl", "+569", "Dir", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.ACTIVO, EstadoBusqueda.BUSCANDO, null
        );
        Propietario entidad = new Propietario();

        when(propietarioMapper.toEntity(request)).thenReturn(entidad);
        when(propietarioRepository.save(entidad)).thenReturn(entidad);

        org.springframework.web.multipart.MultipartFile imageMock = mock(org.springframework.web.multipart.MultipartFile.class);
        when(imageMock.isEmpty()).thenReturn(false);
        when(imageMock.getBytes()).thenThrow(new java.io.IOException("Error de disco simulado"));

        propietarioService.save(request, imageMock);

        verify(propietarioRepository, times(1)).save(entidad);
    }
    @Test
    @DisplayName("Debería mantener los estados en register si la entidad ya los trae y saltarse los defaults")

    void register_WhenEstadosNotNulos_ShouldNotAssignDefaults() {
        PropietarioRequestDto request = new PropietarioRequestDto(
                "Fade", "fade@u.cl", "+569", "Dir", "S", "1",
                TipoPropietario.NATURAL, EstadoCuenta.SUSPENDIDO, null, "image"
        );

        Propietario entidadMapeada = new Propietario();
        entidadMapeada.setEstadoCuenta(EstadoCuenta.SUSPENDIDO);
        entidadMapeada.setEstadoBusqueda(EstadoBusqueda.SIN_MASCOTAS_PERDIDAS);

        when(propietarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(propietarioMapper.toEntity(any(PropietarioRequestDto.class))).thenReturn(entidadMapeada);
        when(propietarioRepository.save(any(Propietario.class))).thenReturn(entidadMapeada);

        PropietarioResponseDto responseSimulado = new PropietarioResponseDto(
                UUID.randomUUID(), "Fade", "fade@u.cl", "+569", "Dir",
                TipoPropietario.NATURAL, EstadoCuenta.SUSPENDIDO, EstadoBusqueda.SIN_MASCOTAS_PERDIDAS, "image"
        );
        when(propietarioMapper.toResponseDto(any(Propietario.class))).thenReturn(responseSimulado);

        propietarioService.register(request);

        assertEquals(EstadoCuenta.SUSPENDIDO, entidadMapeada.getEstadoCuenta());
        assertEquals(EstadoBusqueda.SIN_MASCOTAS_PERDIDAS, entidadMapeada.getEstadoBusqueda());
    }
}