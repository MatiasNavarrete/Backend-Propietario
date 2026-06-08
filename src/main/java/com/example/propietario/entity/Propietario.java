package com.example.propietario.entity;


import com.example.propietario.enums.EstadoBusqueda;
import com.example.propietario.enums.EstadoCuenta;
import com.example.propietario.enums.TipoPropietario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "propietarios", indexes = {
        @Index(name = "idx_propietario_email", columnList = "email")
})
@SQLDelete(sql = "UPDATE propietarios SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
@Getter @Setter
@NoArgsConstructor
public class Propietario extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private String secondaryContactName;
    private String secondaryContactPhone;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPropietario tipoPropietario;

    @Enumerated(EnumType.STRING)
    private EstadoCuenta estadoCuenta;

    @Enumerated(EnumType.STRING)
    private EstadoBusqueda estadoBusqueda;

    @Lob
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;
}
