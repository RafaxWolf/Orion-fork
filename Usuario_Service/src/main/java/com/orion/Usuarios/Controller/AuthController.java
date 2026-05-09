package com.orion.Usuarios.Controller;


import com.orion.Usuarios.DTO.AuthResponse;
import com.orion.Usuarios.DTO.LoginRequest;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Repository.UsuarioRepository;
import com.orion.Usuarios.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:5173") // habilitar React js
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

//        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        final String jwt = jwtUtil.generateToken(usuario);

        return ResponseEntity.ok(new AuthResponse(jwt,usuario.getId()));


    }

}
