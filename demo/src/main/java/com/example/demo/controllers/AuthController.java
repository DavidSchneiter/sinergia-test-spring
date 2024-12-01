package com.example.demo.controllers;

import com.example.demo.DTOs.LoginDto;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//Controlador para la autentificaccion y el registro
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya está en uso.");
        }
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto user) {
        //Busco usuario segundo email
        Optional<User> foundUser = userService.findByEmail(user.getEmail());

        // Si no existe el email o las contraseñas no coinciden devuelve un status 401 y el mensaje
        if (foundUser.isEmpty() ||
                !new BCryptPasswordEncoder().matches(user.getPassword(), foundUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }

        // Devuelve el token para utilizarse en los endpoints (no implementado)
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
}