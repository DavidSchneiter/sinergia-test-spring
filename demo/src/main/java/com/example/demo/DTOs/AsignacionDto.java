package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Data Transfer Object para los objectos que vienen del front
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDto {

    private String name;

    private LocalDateTime dateAsignacion;
}
