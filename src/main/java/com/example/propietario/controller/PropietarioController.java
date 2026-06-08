package com.example.propietario.controller;

import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.service.PropietarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Propietario", description = "Endpoints para la gestión de dueños de mascotas")
@CrossOrigin(origins = "*") //Permitimos llamadas desde el BFF
@RestController
@RequestMapping("/api/v1/propietario")
@RequiredArgsConstructor
public class PropietarioController {

    private final PropietarioService service;

    @Operation(summary = "Registrar nuevo propietario", description = "Recibe el DTO con la imagen en Base64")
    @PostMapping
    public ResponseEntity<PropietarioResponseDto> register(@Valid @RequestBody PropietarioRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }

    @Operation(summary = "Obtener todos los propietarios")
    @GetMapping
    public ResponseEntity<List<PropietarioResponseDto>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener propietario por id")
    @GetMapping("/{id}")
    public ResponseEntity<PropietarioResponseDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Borrar propietario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeOwner(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}