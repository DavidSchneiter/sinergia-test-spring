package com.example.demo.DTOs;

import com.example.demo.models.Colaborador;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

//Data Transfer Object para los objectos que vienen del front
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColaboradorDTO {

    @Id
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
    private String apellido;

    private String telefono;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "Debe proporcionar un correo electrónico válido.")
    private String email;

    @NotNull(message = "El género es obligatorio.")
    private Colaborador.Gender genero;

    private boolean inactivo;

    private LocalDate inicioAusencia;
    private LocalDate finAusencia;

    @NotEmpty(message = "Debe asignar al menos una tarea.")
    private List<String> tareas;  // Recibimos un array de nombres de tareas

    private List<AsignacionDto> asignaciones;  // Recibimos un array de nombres de tareas
}
