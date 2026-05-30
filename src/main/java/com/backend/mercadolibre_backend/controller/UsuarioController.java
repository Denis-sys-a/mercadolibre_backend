package com.backend.mercadolibre_backend.controller;

import com.backend.mercadolibre_backend.model.Usuario;
import com.backend.mercadolibre_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> body) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombres(body.get("nombres"));
            usuario.setApellidos(body.get("apellidos"));
            usuario.setCorreo(body.get("correo"));
            usuario.setPassword(body.get("password"));
            usuario.setRol(Usuario.Rol.valueOf(body.get("rol")));

            Map<String, Object> respuesta = usuarioService.registrar(usuario);
            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Map<String, Object> respuesta = usuarioService.login(
                    body.get("correo"),
                    body.get("password"));
            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
