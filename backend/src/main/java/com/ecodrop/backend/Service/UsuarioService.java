package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.UsuarioDTO;
import com.ecodrop.backend.DTO.UsuarioRegistroDTO;
import com.ecodrop.backend.Exceptions.EmailRegistradoException;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Model.Enum.Rol;
import com.ecodrop.backend.Repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO registrar(@NonNull UsuarioRegistroDTO registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new EmailRegistradoException("Ese email ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setEmail(registroDTO.getEmail());

        usuario.setTelefono(registroDTO.getTelefono());
        
        usuario.setDireccionEntrega(registroDTO.getDireccion());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
        usuario.setRol(Rol.ROLE_USUARIO);

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToDTO(guardado);
    }

    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado con email: " + email));
        return mapToDTO(usuario);
    }

    private UsuarioDTO mapToDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setTelefono(u.getTelefono());
        dto.setDireccionEntrega(u.getDireccionEntrega());
        dto.setRol(u.getRol());
        return dto;
    }
}
