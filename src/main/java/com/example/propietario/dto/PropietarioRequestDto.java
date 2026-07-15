        package com.example.propietario.dto;

        import com.example.propietario.enums.EstadoBusqueda;
        import com.example.propietario.enums.EstadoCuenta;
        import com.example.propietario.enums.TipoPropietario;
        import io.swagger.v3.oas.annotations.media.Schema;
        import jakarta.validation.constraints.Email;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.NotNull;
        import jakarta.validation.constraints.Pattern;

        public record PropietarioRequestDto(
                @Schema(description = "Nombre completo del dueño", example = "Pepe")
                @NotBlank(message = "El nombre es obligatorio")
                String name,

                @Schema(description = "Nombre completo de la mascota", example = "Peru")
                @NotBlank(message = "El nombre es obligatorio")
                String mascotaNombre,

                @Schema(description = "Correo electrónico institucional o personal", example = "fade@u.cl")
                @NotBlank(message = "El email es obligatorio")
                @Email(message = "Debes ingresar un email valido")
                String email,

                @Schema(description = "Teléfono de contacto con formato chileno", example = "+56912345678")
                @NotBlank(message = "El numero de telefono es obligatorio")
                @Pattern(regexp = "^\\+569[0-9]{8}$", message = "El formato debe ser +569XXXXXXXX")
                String phoneNumber,

                @Schema(description = "Dirección de residencia", example = "Calle Falsa 123")
                String address,

                String secondaryContactName,

                String secondaryContactPhone,

                @Schema(description = "Tipo de vinculación", example = "NATURAL")
                @NotNull(message = "El tipo de propietario es obligatorio")
                TipoPropietario tipoPropietario,

                EstadoCuenta estadoCuenta,

                EstadoBusqueda estadoBusqueda,

                String image
        ) {
        }
