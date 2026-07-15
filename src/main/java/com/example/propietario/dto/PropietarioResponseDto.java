package com.example.propietario.dto;

import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;

import java.util.UUID;

public record PropietarioResponseDto(

        UUID id,
        String name,
        String mascotaNombre,
        String email,
        String phoneNumber,
        String address,
        TipoPropietario tipoPropietario,
        EstadoCuenta estadoCuenta,
        EstadoBusqueda estadoBusqueda,
        String image
) {
}
