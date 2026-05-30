package com.backend.mercadolibre_backend.service;

import com.backend.mercadolibre_backend.model.Usuario;
import com.backend.mercadolibre_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public Map<String, Object> registrar(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Encriptar las contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario guardado = usuarioRepository.save(usuario);
        String token = jwtService.generarToken(guardado.getCorreo(), guardado.getRol().name());

        return Map.of(
                "id", guardado.getId(),
                "nombres", guardado.getNombres(),
                "apellidos", guardado.getApellidos(),
                "correo", guardado.getCorreo(),
                "telefono", guardado.getTelefono(),
                "rol", guardado.getRol().name(),
                "token", token);
    }

    public Map<String, Object> login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        // Verificar contraseña encriptada
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }

        String token = jwtService.generarToken(usuario.getCorreo(), usuario.getRol().name());

        return Map.of(
                "id", usuario.getId(),
                "nombres", usuario.getNombres(),
                "apellidos", usuario.getApellidos(),
                "correo", usuario.getCorreo(),
                "rol", usuario.getRol().name(),
                "token", token);
    }
}