package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.ComercioRegistroDTO;
import com.ecodrop.backend.DTO.LoginDTO;
import com.ecodrop.backend.DTO.RepartidorRegistroDTO;
import com.ecodrop.backend.Exceptions.EmailRegistradoException;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Model.Enum.Rol;
import com.ecodrop.backend.Model.Enum.Vehiculo;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.RepartidorRepository;
import com.ecodrop.backend.Security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ComercioLocalRepository comercioRepository;
    private final RepartidorRepository repartidorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          ComercioLocalRepository comercioRepository, RepartidorRepository repartidorRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.comercioRepository = comercioRepository;
        this.repartidorRepository = repartidorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registrar/comercio")
    public ResponseEntity<?> registrarComercio(@Valid @RequestBody ComercioRegistroDTO dto) {
        if (comercioRepository.existsByEmail(dto.getEmail()) || repartidorRepository.existsByEmail(dto.getEmail())) {
            throw new EmailRegistradoException("Ese email ya está registrado.");
        }

        ComercioLocal comercio = new ComercioLocal();
        comercio.setNombreComercio(dto.getNombreComercio());
        comercio.setCategoria(dto.getCategoria());
        comercio.setDireccionComercio(dto.getDireccionComercio());
        comercio.setLogo(dto.getLogo());
        comercio.setTelefono(dto.getTelefono());
        comercio.setHorarioApertura(dto.getHorarioApertura());
        comercio.setEmail(dto.getEmail());
        comercio.setPassword(passwordEncoder.encode(dto.getPassword()));
        comercio.setRol(Rol.ROLE_COMERCIO);
        comercioRepository.save(comercio);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Comercio registrado correctamente");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar/repartidor")
    public ResponseEntity<?> registrarRepartidor(@Valid @RequestBody RepartidorRegistroDTO dto) {
        if (comercioRepository.existsByEmail(dto.getEmail()) || repartidorRepository.existsByEmail(dto.getEmail())) {
            throw new EmailRegistradoException("Ese email ya está registrado.");
        }

        Repartidor repartidor = new Repartidor();
        repartidor.setNombre(dto.getNombre());
        repartidor.setApellidos(dto.getApellidos());
        repartidor.setTelefono(dto.getTelefono());
        repartidor.setVehiculo(Vehiculo.valueOf(dto.getVehiculo()));
        repartidor.setEstado(EstadoRepartidor.DISPONIBLE);
        repartidor.setDisponibilidad(true);
        repartidor.setEmail(dto.getEmail());
        repartidor.setPassword(passwordEncoder.encode(dto.getPassword()));
        repartidor.setRol(Rol.ROLE_REPARTIDOR);
        repartidorRepository.save(repartidor);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Repartidor registrado correctamente");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication.getName());

        String rol = obtenerRol(authentication.getName());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("email", authentication.getName());
        response.put("rol", rol);

        return ResponseEntity.ok(response);
    }

    private String obtenerRol(String email) {
        var comercio = comercioRepository.findByEmail(email);
        if (comercio.isPresent()) {
            return comercio.get().getRol().name().replace("ROLE_", "");
        }
        var repartidor = repartidorRepository.findByEmail(email);
        if (repartidor.isPresent()) {
            return repartidor.get().getRol().name().replace("ROLE_", "");
        }
        throw new RuntimeException("Cuenta no encontrada");
    }
}
