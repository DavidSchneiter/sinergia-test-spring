package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tarea es obligatorio.")
    private String name;

    // Relación con Collaborator
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false) // Clave foránea
    @JsonBackReference
    private Colaborador colaborador;
}