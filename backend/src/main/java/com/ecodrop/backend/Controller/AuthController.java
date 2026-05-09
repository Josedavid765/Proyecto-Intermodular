package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.LoginDTO;
import com.ecodrop.backend.DTO.UsuarioDTO;
import com.ecodrop.backend.DTO.UsuarioRegistroDTO;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Repository.UsuarioRepository;
import com.ecodrop.backend.Security.JwtUtils;
import com.ecodrop.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("null")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService, JwtUtils jwtUtils, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        return ResponseEntity.ok(usuarioService.registrar(registroDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication.getName());

        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("email", authentication.getName());
        response.put("rol", usuario.getRol().name().replace("ROLE_", ""));
        
        return ResponseEntity.ok(response);
    }
}
