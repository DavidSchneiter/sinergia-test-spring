package com.example.demo.services;

import com.example.demo.DTOs.AsignacionDto;
import com.example.demo.DTOs.ColaboradorDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.models.Asignacion;
import com.example.demo.models.Colaborador;
import com.example.demo.models.Tarea;
import com.example.demo.repos.AsignacionRepository;
import com.example.demo.repos.ColaboradorRepository;
import com.example.demo.repos.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;
    @Autowired
    private TareaRepository taskRepository;
    @Autowired
    private AsignacionRepository asignacionRepository;

    //Devuelve todos los coalaboradores modificados para el front
    public List<ColaboradorDTO> getAllColaboradors() {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();

        // Convertir cada Colaborador en un ColaboradorDTO
        return colaboradores.stream().map(colaborador ->
                ColaboradorDTO.builder()
                        .id(colaborador.getId())
                        .nombre(colaborador.getNombre())
                        .apellido(colaborador.getApellido())
                        .telefono(colaborador.getTelefono())
                        .email(colaborador.getEmail())
                        .genero(colaborador.getGenero())
                        .inactivo(colaborador.isInactivo())
                        .inicioAusencia(colaborador.getInicioAusencia())
                        .finAusencia(colaborador.getFinAusencia())
                        // Convertir lista de tareas a en una lista con solo los nombres para manejar la informacion en el front
                        .tareas(colaborador.getTareas().stream()
                                .map(Tarea::getName)
                                .collect(Collectors.toList()))
                        // Convertir lista de asignaciones a en una lista con solo los nombres y fecha de asignacion para manejar la informacion en el front
                        .asignaciones(colaborador.getAsignaciones().stream()
                                .map(asignacion -> AsignacionDto.builder()
                                        .name(asignacion.getName())
                                        .dateAsignacion(asignacion.getDateAsignacion())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());


    }

    //Devuelve un colaborador por id
    public Optional<Colaborador> getColaboradorById(Long id) {
        return colaboradorRepository.findById(id);
    }

    //Crea un colaborador, las tareas y asignaciones
    public Colaborador createColaborador(ColaboradorDTO colaboradorDto) {
        //Si el email es duplicado tira un bad request y el error
        if (colaboradorRepository.existsByEmail(colaboradorDto.getEmail())) {
            throw new DuplicateEmailException("El email ya está registrado: " + colaboradorDto.getEmail());
        }
        //Solo puede existir un usuario con el mismo nombre y apellido
        colaboradorRepository.findByNombreAndApellido(colaboradorDto.getNombre(), colaboradorDto.getApellido())
                .ifPresent(colaborador -> {
                    throw new DuplicateEmailException("Colaborador: " + colaboradorDto.getNombre() + " " + colaboradorDto.getApellido() + " existente");
                });

        // Crear el colaborador sin tareas
        Colaborador colaborador = Colaborador.builder()
                .nombre(colaboradorDto.getNombre())
                .apellido(colaboradorDto.getApellido())
                .telefono(colaboradorDto.getTelefono())
                .email(colaboradorDto.getEmail())
                .genero(colaboradorDto.getGenero())
                .inactivo(colaboradorDto.isInactivo())
                .inicioAusencia(colaboradorDto.getInicioAusencia())
                .finAusencia(colaboradorDto.getFinAusencia())
                .tareas(new ArrayList<>())
                .asignaciones(new ArrayList<>())
                .build();

        // Guardar el colaborador primero (necesitamos su ID para las tareas)
        Colaborador savedCollaborator = colaboradorRepository.save(colaborador);

        List<Asignacion> asignaciones = colaboradorDto.getAsignaciones().stream()
                .map(asignacionName -> Asignacion.builder()
                        .name(String.valueOf(asignacionName.getName()))
                        .colaborador(savedCollaborator)
                        .dateAsignacion(asignacionName.getDateAsignacion())
                        .build())
                .toList();
        // Crear las tareas asociadas al colaborador

        List<Tarea> tasks = colaboradorDto.getTareas().stream()
                        .map(taskName -> Tarea.builder()
                                .name(String.valueOf(taskName))
                                .colaborador(savedCollaborator)
                                .build())
                        .toList();

        // Guardar las tareas en la base de datos
        taskRepository.saveAll(tasks);
        asignacionRepository.saveAll(asignaciones);

        // Devolver el colaborador con las tareas asociadas
        savedCollaborator.setTareas(tasks);
        savedCollaborator.setAsignaciones(asignaciones);
        return savedCollaborator;
    }


    public Optional<Colaborador> updateColaborador(Long id, ColaboradorDTO updatedColaborador) {
            //Si existe colaborador, actualiza los campos y lo guarda en la db
        return colaboradorRepository.findById(id).map(existing -> {
            if (!existing.getEmail().equals(updatedColaborador.getEmail()) &&
                    colaboradorRepository.existsByEmail(updatedColaborador.getEmail())) {
                throw new DuplicateEmailException("El email ya está registrado: " + updatedColaborador.getEmail());
            }
            existing.setNombre(updatedColaborador.getNombre());
            existing.setApellido(updatedColaborador.getApellido());
            existing.setTelefono(updatedColaborador.getTelefono());
            existing.setGenero(updatedColaborador.getGenero());
            existing.setInactivo(updatedColaborador.isInactivo());
            existing.setInicioAusencia(updatedColaborador.getInicioAusencia());
            existing.setFinAusencia(updatedColaborador.getFinAusencia());
            // Buscar las nuevas tareas por sus nombres desde el repositorio

            // Limpiar las tareas y asignaciones actuales del colaborador y asignar solo las nuevas
            existing.getTareas().clear();

            List<Tarea> tasks = updatedColaborador.getTareas().stream()
                    .map(taskName -> Tarea.builder()
                            .name(String.valueOf(taskName))
                            .colaborador(existing)
                            .build())
                    .collect(Collectors.toList());existing.getTareas().clear();

            existing.getAsignaciones().clear();

            List<Asignacion> asignaciones = updatedColaborador.getAsignaciones().stream()
                    .map(asignacionName -> Asignacion.builder()
                            .name(String.valueOf(asignacionName.getName()))
                            .colaborador(existing)
                            .dateAsignacion(asignacionName.getDateAsignacion())
                            .build())
                    .toList();

            // Guardar las tareas y asignaciones actualizadas en la base de datos
            taskRepository.saveAll(tasks);
            asignacionRepository.saveAll(asignaciones);

            return colaboradorRepository.save(existing);
        });
    }

    //Borra un colaborador por id
    public void deleteColaborador(Long id) {
        colaboradorRepository.deleteById(id);
    }

}
