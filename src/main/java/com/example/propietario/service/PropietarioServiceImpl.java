package com.example.propietario.service;


import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.entity.Propietario;
import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.mapper.PropietarioMapper;
import com.example.propietario.repository.PropietarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Inyecta automáticamente el Repository y el Mapper
public class PropietarioServiceImpl implements PropietarioService {

    private final PropietarioRepository repository;
    private final PropietarioMapper mapper;

    @Override
    @Transactional
    public PropietarioResponseDto register(PropietarioRequestDto dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("El email " + dto.email() + " ya está registrado.");
        }

        Propietario entidad = mapper.toEntity(dto);

        // 2. Seteamos los valores por defecto ANTES de guardar
        if (entidad.getEstadoCuenta() == null) {
            entidad.setEstadoCuenta(EstadoCuenta.ACTIVO);
        }
        if (entidad.getEstadoBusqueda() == null) {
            entidad.setEstadoBusqueda(com.example.propietario.enums.EstadoBusqueda.BUSCANDO);
        }

        Propietario guardado = repository.save(entidad);

        return mapper.toResponseDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PropietarioResponseDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("No se encontró el propietario con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Propietario propietario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro el propietario con esta id: " + id));

        repository.delete(propietario);

    }

    @Override
    public List<PropietarioResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public PropietarioResponseDto save(PropietarioRequestDto dto, MultipartFile image) {
        // 1. Usas el mapper para crear la entidad
        Propietario propietario = mapper.toEntity(dto);

        // 2. Procesas la foto (solo lógica, no guardado aún)
        if (image != null && !image.isEmpty()) {
            try {
                String base64 = Base64.getEncoder().encodeToString(image.getBytes());
                propietario.setImage("data:" + image.getContentType() + ";base64," + base64);
            } catch (IOException e) { /* manejo error */ }
        }

        // 3. AQUÍ USAS EL REPOSITORIO (Él hace el trabajo sucio)
        Propietario entidadGuardada = repository.save(propietario);

        // 4. Conviertes a DTO para que React reciba los datos
        return mapper.toResponseDto(entidadGuardada);
    }
}