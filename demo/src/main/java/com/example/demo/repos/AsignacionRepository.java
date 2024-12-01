package com.example.demo.repos;

import com.example.demo.models.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Repositorios de JPA
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByColaboradorId(Long colaboradorId);
}
