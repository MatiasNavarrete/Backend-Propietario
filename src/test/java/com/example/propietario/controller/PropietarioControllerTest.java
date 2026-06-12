package com.example.propietario.controller;

import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;
import com.example.propietario.service.PropietarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PropietarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PropietarioService propietarioService;

    @InjectMocks
    private PropietarioController propietarioController;

    private ObjectMapper objectMapper;
    private PropietarioResponseDto responseDto;
    private PropietarioRequestDto requestDto;
    private UUID propietarioId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(propietarioController).build();
        objectMapper = new ObjectMapper(); // Para transformar el DTO a JSON
        propietarioId = UUID.randomUUID();

        // El DTO que enviamos (Request)
        requestDto = new PropietarioRequestDto(
                "Fade",
                "fade@example.com",
                "+56912345678",
                "Direccion Ficticia",
                "data:image/jpeg;base64,mockbase64...", // Tu imagen ya viene en Base64 según tu Swagger
                null,
                TipoPropietario.NATURAL,
                EstadoCuenta.ACTIVO,
                EstadoBusqueda.BUSCANDO,
                "image"
        );

        // El DTO que devuelve (Response)
        responseDto = new PropietarioResponseDto(
                propietarioId,
                "Fade",
                "fade@example.com",
                "+56912345678",
                "Direccion Ficticia",
                null, null,
                EstadoBusqueda.BUSCANDO,
                "data:image/jpeg;base64,mockbase64..."
        );
    }

    @Test
    @DisplayName("GET /api/v1/propietario debería retornar lista de propietarios")
    void findAll_ShouldReturnList() throws Exception {
        when(propietarioService.findAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/propietario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fade"));
    }

    @Test
    @DisplayName("GET /api/v1/propietario/{id} debería retornar el propietario")
    void findById_ShouldReturnPropietario() throws Exception {
        when(propietarioService.findById(any(UUID.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/propietario/" + propietarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fade"));
    }

    @Test
    @DisplayName("POST /api/v1/propietario debería registrar y retornar 201 CREATED")
    void register_ShouldReturnCreated() throws Exception {
        // Tu método se llama register, no save
        when(propietarioService.register(any(PropietarioRequestDto.class))).thenReturn(responseDto);

        // Enviamos un JSON puro usando ObjectMapper, calzando perfecto con tu @RequestBody
        mockMvc.perform(post("/api/v1/propietario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()) // Esperamos un 201 Created según tu código
                .andExpect(jsonPath("$.name").value("Fade"));
    }

    @Test
    @DisplayName("DELETE /api/v1/propietario/{id} debería eliminar y retornar 204 NO CONTENT")
    void removeOwner_ShouldReturnNoContent() throws Exception {
        doNothing().when(propietarioService).delete(any(UUID.class));

        mockMvc.perform(delete("/api/v1/propietario/" + propietarioId))
                .andExpect(status().isNoContent()); // Esperamos un 204 No Content según tu código

        verify(propietarioService, times(1)).delete(any(UUID.class));
    }
}