package com.orion.Usuarios.Controller;


import com.orion.Usuarios.DTO.*;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
//@CrossOrigin(origins = "http://localhost:5173") // habilitar React js
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    ///  CRUD

    // CREATE

    @PostMapping("/registro")
    public ResponseEntity<RegisterResponse> registrar(@RequestBody RegisterRequest registerRequest) {
        Usuario userRegistrado = usuarioService.registrarUsuario(registerRequest);
        return ResponseEntity.ok(new RegisterResponse(
                userRegistrado.getId(),
                userRegistrado.getUsername(),
                userRegistrado.getEmail()
        ));
    }


    // READ

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id){

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        UsuarioPerfil usuarioPerfil = usuarioService.obtenerUsuarioPerfilPorId(id);
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuarioPerfil.getAvatarUrl(),
                usuarioPerfil.getBiografia(),
                usuarioPerfil.getUbicacion()
        );

        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorUsername(@PathVariable String username){
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(username);
        UsuarioPerfil usuarioPerfil = usuarioService.obtenerUsuarioPerfilPorId(usuario.getId());
        return ResponseEntity.ok(new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuarioPerfil.getAvatarUrl(),
                usuarioPerfil.getBiografia(),
                usuarioPerfil.getUbicacion()
        ));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodosUsuarios(){
        return ResponseEntity.ok(usuarioService.obtenerTodosUsuarios());
    }


    // UPDATE


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO updateDTO
    ){
        UsuarioResponseDTO actualizado = usuarioService.actualizarUsuario(id, updateDTO);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE


    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodosUsuarios(){}






    ///  FRONTEND APP

    @GetMapping("/profile/photo/{userId}")
    public ResponseEntity<ProfilePhotoDTO> buscarAvatarUrl(@PathVariable Long userId){
        UsuarioPerfil usuarioPerfil = usuarioService.obtenerUsuarioPerfilPorId(userId);
        return ResponseEntity.ok(new ProfilePhotoDTO(usuarioPerfil.getAvatarUrl()));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUsuarioPerfil(@PathVariable Long userId){
        Usuario user =  usuarioService.obtenerUsuarioPorId(userId);

        UserProfileResponse perfil = new UserProfileResponse(user.getUsername(),user.getPerfil().getAvatarUrl(),
                user.getPerfil().getBiografia(),user.getPerfil().getUbicacion());

        return ResponseEntity.ok(perfil);
    }


    @PutMapping("/{id}/avatar")
    public ResponseEntity<UsuarioPerfil> actualizarUrlAvatar(@PathVariable Long id, @RequestBody Map<String, String> body){

        String nuevaUrl = body.get("avatarUrl");

        UsuarioPerfil perfilActualizar = usuarioService.actualizarUrlAvatar(id, nuevaUrl);

        return ResponseEntity.ok(perfilActualizar);
    }





    /// ADMIN ONLY

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id){
//        usuarioService.eliminarUsuario(id);
//
//
//        return ResponseEntity.ok("Usuario eliminado con exito por el administrador");
//    }

}
