package com.example.propietario.mapper;

import com.example.propietario.dto.PropietarioRequestDto;
import com.example.propietario.dto.PropietarioResponseDto;
import com.example.propietario.entity.Propietario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropietarioMapper {

    // Convierte el DTO de entrada a la Entidad de base de datos
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "_active", ignore = true)
    @Mapping(target = "image", source = "image")
    Propietario toEntity(PropietarioRequestDto dto);

    // Convierte la Entidad al DTO de salida (lo que ve el cliente)
    @Mapping(source = "image", target = "image")
    PropietarioResponseDto toResponseDto(Propietario entity);

}