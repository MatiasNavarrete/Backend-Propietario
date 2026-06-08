package com.example.propietario.repository;

import com.example.propietario.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, UUID> {

    Optional<Propietario> findByEmail(String email);

    boolean existsByEmail(String email);

}
