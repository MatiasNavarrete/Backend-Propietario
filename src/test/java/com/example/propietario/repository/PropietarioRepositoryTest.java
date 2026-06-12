package com.example.propietario.repository;

import com.example.propietario.entity.Propietario;
import com.example.propietario.enums.EstadoBusqueda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PropietarioRepositoryTest {

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Test
    @DisplayName("Debería guardar y encontrar un Propietario por su ID en la base de datos")
    void saveAndFindById_ShouldReturnPropietario() {
        Propietario propietario = new Propietario();
        propietario.setName("Fade");
        propietario.setEmail("fade@example.com");
        propietario.setPhoneNumber("+56912345678");
        propietario.setAddress("Direccion Ficticia");
        propietario.setEstadoBusqueda(EstadoBusqueda.BUSCANDO);
        propietario.setImage("data:image/jpeg;base64,mock...");

        Propietario savedPropietario = propietarioRepository.save(propietario);
        Optional<Propietario> foundPropietario = propietarioRepository.findById(savedPropietario.getId());

        assertTrue(foundPropietario.isPresent());
        assertEquals("Fade", foundPropietario.get().getName());
        assertEquals("fade@example.com", foundPropietario.get().getEmail());
        assertEquals(EstadoBusqueda.BUSCANDO, foundPropietario.get().getEstadoBusqueda());
    }

    @Test
    @DisplayName("Debería retornar un Optional vacío al buscar un ID que no existe")
    void findById_WhenIdDoesNotExist_ShouldReturnEmpty() {
        java.util.UUID fakeId = java.util.UUID.randomUUID();

        Optional<Propietario> foundPropietario = propietarioRepository.findById(fakeId);

        assertTrue(foundPropietario.isEmpty());
    }
}