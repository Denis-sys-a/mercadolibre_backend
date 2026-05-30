package com.backend.mercadolibre_backend.service;

import com.backend.mercadolibre_backend.model.Usuario;
import com.backend.mercadolibre_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Map<String, Object> registrar(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        Usuario guardado = usuarioRepository.save(usuario);
        return Map.of(
                "id", guardado.getId(),
                "nombres", guardado.getNombres(),
                "apellidos", guardado.getApellidos(),
                "telefono", guardado.getTelefono(),
                "correo", guardado.getCorreo(),
                "rol", guardado.getRol().name(),
                "token", "token-" + guardado.getId());
    }

    public Map<String, Object> login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }

        return Map.of(
                "id", usuario.getId(),
                "nombres", usuario.getNombres(),
                "apellidos", usuario.getApellidos(),
                "correo", usuario.getCorreo(),
                "rol", usuario.getRol().name(),
                "token", "token-" + usuario.getId());
    }
}
