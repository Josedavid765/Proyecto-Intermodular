package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.UsuarioDTO;
import com.ecodrop.backend.DTO.UsuarioRegistroDTO;
import com.ecodrop.backend.Exceptions.EmailRegistradoException;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Model.Enum.Rol;
import com.ecodrop.backend.Repository.UsuarioRepository;
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

    public UsuarioDTO registrar(UsuarioRegistroDTO registroDTO) {
    if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
        throw new EmailRegistradoException("Ese email ya está registrado.");
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(registroDTO.getNombre());
    usuario.setApellido(registroDTO.getApellido());
    usuario.setEmail(registroDTO.getEmail());

    usuario.setTelefono(registroDTO.getTelefono()); 
    
    usuario.setDireccionEntrega(registroDTO.getDireccionEntrega());
    usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
    usuario.setRol(registroDTO.getRol() != null ? registroDTO.getRol() : Rol.ROLE_USUARIO);

    Usuario guardado = usuarioRepository.save(usuario);
    return mapToDTO(guardado);
}

private UsuarioDTO mapToDTO(Usuario u) {
    UsuarioDTO dto = new UsuarioDTO();
    dto.setIdUsuario(u.getIdUsuario());
    dto.setNombre(u.getNombre());
    dto.setApellido(u.getApellido());
    dto.setEmail(u.getEmail());
    dto.setTelefono(u.getTelefono()); 
    dto.setDireccionEntrega(u.getDireccionEntrega());
    return dto;
}
}