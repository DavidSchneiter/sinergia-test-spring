package com.example.demo.controllers;

import com.example.demo.DTOs.ColaboradorDTO;
import com.example.demo.models.Colaborador;
import com.example.demo.services.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador de los colaboradores
@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    @Autowired
    private  ColaboradorService colaboradorService;

    // Devuelve un 200 y la lista de los colaboradores
    @GetMapping
    public ResponseEntity<List<ColaboradorDTO>> getAllColaboradors() {
        List<ColaboradorDTO> colaboradores = colaboradorService.getAllColaboradors();
        return ResponseEntity.ok(colaboradores);
    }

    // Devuelve un 200 si encuentra el colaborador segun la id y un 404 si no
    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> getColaboradorById(@PathVariable Long id) {
        return colaboradorService.getColaboradorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Devuelve un 200 si se crea el colaborador
    @PostMapping
    public ResponseEntity<Colaborador> createColaborador(@Valid @RequestBody ColaboradorDTO colaborador) {
        Colaborador createdColaborador = colaboradorService.createColaborador(colaborador);
        return ResponseEntity.ok(createdColaborador);
    }

    // Devuelve un 200 si actualiza el colaborador segun la id y un 404 si no
    @PutMapping("/{id}")
    public ResponseEntity<Colaborador> updateColaborador(@PathVariable Long id, @Valid @RequestBody ColaboradorDTO updatedColaborador) {
        return colaboradorService.updateColaborador(id, updatedColaborador)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Borra un colaborador segun la id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColaborador(@PathVariable Long id) {
        colaboradorService.deleteColaborador(id);
        return ResponseEntity.noContent().build();
    }

}