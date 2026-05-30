package com.backend.mercadolibre_backend.controller;

import com.backend.mercadolibre_backend.model.Usuario;
import com.backend.mercadolibre_backend.repository.UsuarioRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> body) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombres(body.get("nombres"));
            usuario.setApellidos(body.get("apellidos"));
            usuario.setCorreo(body.get("correo"));
            usuario.setPassword(body.get("password"));
            usuario.setTelefono(body.get("telefono"));
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

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody Map<String, String> body) {
        try {
            Map<String, Object> respuesta = usuarioService.registrarOLoginGoogle(
                    body.get("nombres"),
                    body.get("apellidos"),
                    body.get("correo"));
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/google/session")
    public ResponseEntity<?> sesionGoogle(@RequestParam String correo) {
        try {
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            return ResponseEntity.ok(Map.of(
                    "id", usuario.getId(),
                    "nombres", usuario.getNombres(),
                    "correo", usuario.getCorreo(),
                    "rol", usuario.getRol().name()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}