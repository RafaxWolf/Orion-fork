package com.orion.Usuarios.Service;


import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Repository.RolRepository;
import com.orion.Usuarios.Repository.UserProfileRepository;
import com.orion.Usuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProfileRepository userProfileRepository;



    // CRUD COMPLETO

    // CREATE

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));


        Rol userRole = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol no existe"));


        Set<Rol> roles = new HashSet<>();

        roles.add(userRole);
        usuario.setRoles(roles);

        Usuario userGuardado = usuarioRepository.save(usuario);

        UsuarioPerfil perfilVacio = new UsuarioPerfil();
        perfilVacio.setUsuario(userGuardado);
        perfilVacio.setNombreDisplay(userGuardado.getUsername());
        perfilVacio.setAvatarUrl("/api/media/avatar/default_avatar.png");
        userProfileRepository.save(perfilVacio);

        return userGuardado;


    }


    // READ
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
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

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado){
        Usuario usuarioExistente = obtenerUsuarioPorId(id);

        if (usuarioActualizado.getUsername() != null &&
        !usuarioActualizado.getUsername().equals(usuarioExistente.getUsername())) {

            usuarioExistente.setUsername(usuarioActualizado.getUsername());
        }

        if (usuarioActualizado.getPassword() != null
        && !usuarioActualizado.getPassword().isEmpty()){
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }


    public void eliminarUsuario(Long id){

        if(!usuarioRepository.existsById(id)){
            throw new RuntimeException("Usuario no encontrado con el id");
        }

        ///  borrará el usuario y el userProfile juntos como cascada
        usuarioRepository.deleteById(id);

    }


}
