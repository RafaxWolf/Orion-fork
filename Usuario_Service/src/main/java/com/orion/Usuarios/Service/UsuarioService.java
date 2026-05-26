package com.orion.Usuarios.Service;


import com.orion.Usuarios.DTO.RegisterRequest;
import com.orion.Usuarios.DTO.UsuarioResponseDTO;
import com.orion.Usuarios.DTO.UsuarioUpdateDTO;
import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Repository.RolRepository;
import com.orion.Usuarios.Repository.UserProfileRepository;
import com.orion.Usuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioService {

    // inyecciones
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserProfileRepository userProfileRepository;



    // CRUD

    // CREATE
    public Usuario registrarUsuario(RegisterRequest registerRequest) {
        // se crea usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // se crea el perfil de usuario
        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
        usuarioPerfil.setBiografia(registerRequest.getBiografia());
        usuarioPerfil.setAvatarUrl("/api/media/avatar/default_avatar.png");
        usuarioPerfil.setUbicacion(registerRequest.getUbicacion());
        usuario.setPerfil(usuarioPerfil);

        // se asigna rol predeterminado
        Rol userRole = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol no existe"));
        Set<Rol> roles = new HashSet<>();
        roles.add(userRole);
        usuario.setRoles(roles);

        // se guarda
        Usuario userGuardado = usuarioRepository.save(usuario);
        return userGuardado;
    }


    // READ
    public List<UsuarioResponseDTO> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> usuarioResponseDTOs = new ArrayList<>();

        for (Usuario u : usuarios) {
            usuarioResponseDTOs.add(new UsuarioResponseDTO(
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getPerfil().getAvatarUrl(),
                    u.getPerfil().getBiografia(),
                    u.getPerfil().getUbicacion()
            ));
        }

        return usuarioResponseDTOs;
    }

    public Usuario obtenerUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));
    }

    public Usuario obtenerUsuarioPorUsername(String username){
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el username"));
    }

    public UsuarioPerfil obtenerUsuarioPerfilPorId(Long id){
        return userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));
    }




    // UPDATE

    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));

        UsuarioPerfil usuarioPerfil = userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));


        if (usuarioUpdateDTO.getUsername() != null && !usuarioUpdateDTO.getUsername().isEmpty()) {
            usuario.setUsername(usuarioUpdateDTO.getUsername());
        }

        if (usuarioUpdateDTO.getEmail() != null && !usuarioUpdateDTO.getEmail().isEmpty()) {
            usuario.setEmail(usuarioUpdateDTO.getEmail());
        }

        if (usuarioUpdateDTO.getPassword() != null && !usuarioUpdateDTO.getPassword().isEmpty()) {
            usuario.setPassword(usuarioUpdateDTO.getPassword());
        }

        if (usuarioUpdateDTO.getAvatarUrl() != null && !usuarioUpdateDTO.getAvatarUrl().isEmpty()) {
            usuarioPerfil.setAvatarUrl(usuarioUpdateDTO.getAvatarUrl());
        }

        if (usuarioUpdateDTO.getBiografia() != null && !usuarioUpdateDTO.getBiografia().isEmpty()) {
            usuarioPerfil.setBiografia(usuarioUpdateDTO.getBiografia());
        }

        if (usuarioUpdateDTO.getUbicacion() != null && !usuarioUpdateDTO.getUbicacion().isEmpty()) {
            usuarioPerfil.setUbicacion(usuarioUpdateDTO.getUbicacion());
        }

        usuarioRepository.save(usuario);
        userProfileRepository.save(usuarioPerfil);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPerfil().getAvatarUrl(),
                usuario.getPerfil().getBiografia(),
                usuario.getPerfil().getUbicacion()
        );
    }

    // DELETE
    public void eliminarUsuario(Long id){

        if(!usuarioRepository.existsById(id)){
            throw new RuntimeException("Usuario no encontrado con el id");
        }

        ///  borrará el usuario y el userProfile juntos como cascada
        usuarioRepository.deleteById(id);

    }

//    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado){
//        Usuario usuarioExistente = obtenerUsuarioPorId(id);
//
//        if (usuarioActualizado.getUsername() != null &&
//        !usuarioActualizado.getUsername().equals(usuarioExistente.getUsername())) {
//
//            usuarioExistente.setUsername(usuarioActualizado.getUsername());
//        }
//
//        if (usuarioActualizado.getPassword() != null
//        && !usuarioActualizado.getPassword().isEmpty()){
//            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
//        }
//
//        return usuarioRepository.save(usuarioExistente);
//    }

    public UsuarioPerfil actualizarUrlAvatar(Long id, String nuevaUrl){
        UsuarioPerfil usuarioPerfil = userProfileRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id"));

        usuarioPerfil.setAvatarUrl(nuevaUrl);

        return userProfileRepository.save(usuarioPerfil);
    }





}
