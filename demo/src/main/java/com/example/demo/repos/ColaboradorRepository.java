package com.example.demo.repos;

import com.example.demo.models.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    Optional<Colaborador> findByNombreAndApellido(String nombre, String apellido);
}
