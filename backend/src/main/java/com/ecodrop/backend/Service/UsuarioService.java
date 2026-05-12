package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.UsuarioDTO;
import com.ecodrop.backend.DTO.UsuarioRegistroDTO;
import com.ecodrop.backend.Exceptions.EmailRegistradoException;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Model.Enum.Rol;
import com.ecodrop.backend.Model.Enum.Vehiculo;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.RepartidorRepository;
import com.ecodrop.backend.Repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ComercioLocalRepository comercioRepository;
    private final RepartidorRepository repartidorRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder,
                          ComercioLocalRepository comercioRepository, RepartidorRepository repartidorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.comercioRepository = comercioRepository;
        this.repartidorRepository = repartidorRepository;
    }

    public UsuarioDTO registrar(@NonNull UsuarioRegistroDTO registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new EmailRegistradoException("Ese email ya está registrado.");
        }

        String rol = registroDTO.getRol() != null ? registroDTO.getRol().toUpperCase() : "USUARIO";

        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setTelefono(registroDTO.getTelefono());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        if ("COMERCIO".equals(rol) || "REPARTIDOR".equals(rol)) {
            usuario.setDireccionEntrega(registroDTO.getDireccion());
        } else {
            usuario.setDireccionEntrega(registroDTO.getDireccion());
        }

        usuario.setRol(Rol.valueOf("ROLE_" + rol));
        Usuario guardado = usuarioRepository.save(usuario);

        if ("COMERCIO".equals(rol)) {
            ComercioLocal comercio = new ComercioLocal();
            comercio.setNombreComercio(registroDTO.getNombreComercio());
            comercio.setCategoria(registroDTO.getCategoria());
            comercio.setDireccionComercio(registroDTO.getDireccionComercio());
            comercio.setLogo(registroDTO.getLogo());
            comercio.setTelefono(registroDTO.getTelefono());
            comercio.setHorarioApertura(registroDTO.getHorarioApertura());
            comercio.setUsuario(guardado);
            comercioRepository.save(comercio);
        } else if ("REPARTIDOR".equals(rol)) {
            Repartidor repartidor = new Repartidor();
            repartidor.setNombre(registroDTO.getNombre());
            repartidor.setApellidos(registroDTO.getApellido());
            repartidor.setTelefono(registroDTO.getTelefono());
            repartidor.setVehiculo(Vehiculo.valueOf(registroDTO.getVehiculo()));
            repartidor.setEstado(EstadoRepartidor.DISPONIBLE);
            repartidor.setDisponibilidad(true);
            repartidor.setUsuario(guardado);
            repartidorRepository.save(repartidor);
        }

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
