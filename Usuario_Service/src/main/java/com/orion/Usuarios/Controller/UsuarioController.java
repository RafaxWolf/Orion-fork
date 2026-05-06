package com.orion.Usuarios.Controller;


import com.orion.Usuarios.DTO.RegisterRequest;
import com.orion.Usuarios.DTO.RegisterResponse;
import com.orion.Usuarios.DTO.UsuarioResponseDTO;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
//@CrossOrigin(origins = "http://localhost:5173") // habilitar React js
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity<RegisterResponse> registrar(@RequestBody RegisterRequest registerRequest) {
        Usuario user = new Usuario();
        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
        usuarioPerfil.setBiografia(registerRequest.getBiografia());
        usuarioPerfil.setAvatarUrl(registerRequest.getAvatarUrl());
        usuarioPerfil.setUbicacion(registerRequest.getUbicacion());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setPerfil(usuarioPerfil);
        Usuario userRegistrado = usuarioService.registrarUsuario(user);
        return ResponseEntity.ok(new RegisterResponse(
                userRegistrado.getId(),
                userRegistrado.getUsername(),
                userRegistrado.getEmail()
        ));
    }

//    @PostMapping("/registro")
//    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario){
//        Usuario nuevoUser = usuarioService.registrarUsuario(usuario);
//        return ResponseEntity.ok(nuevoUser);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id){

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        UsuarioPerfil usuarioPerfil = usuarioService.obtenerUsuarioPerfilPorId(id);
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuarioPerfil.getAvatarUrl(),
                usuarioPerfil.getBiografia(),
                usuarioPerfil.getUbicacion()
        );

        return ResponseEntity.ok(usuarioResponseDTO);
    }




    /// ADMIN ONLY

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);


        return ResponseEntity.ok("Usuario eliminado con exito por el administrador");
    }

}
