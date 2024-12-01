package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    @Column(nullable = false)
    private String apellido;

    private String telefono;  // No obligatorio

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "Debe proporcionar un correo electrónico válido.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "El género es obligatorio.")
    @Enumerated(EnumType.STRING)
    private Gender genero;

    private boolean inactivo;  // Si está inactivo para asignaciones futuras

    private LocalDate inicioAusencia;  // Fecha de inicio del período de ausencia
    private LocalDate finAusencia;    // Fecha de finalización del período de ausencia

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Tarea> tareas = new ArrayList<>();  // Lista de tareas asignadas

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Asignacion> asignaciones = new ArrayList<>();

    public enum Gender {
        MASCULINO, FEMENINO
    }
}
