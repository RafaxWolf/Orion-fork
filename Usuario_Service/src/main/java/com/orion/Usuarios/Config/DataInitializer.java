package com.orion.Usuarios.Config;

import com.orion.Usuarios.Entity.Permiso;
import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Entity.Usuario;
import com.orion.Usuarios.Entity.UsuarioPerfil;
import com.orion.Usuarios.Repository.PermisoRepository;
import com.orion.Usuarios.Repository.RolRepository;
import com.orion.Usuarios.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
public class DataInitializer {


    @Bean
    public CommandLineRunner initData(RolRepository rolRepository, PermisoRepository permisoRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            if (rolRepository.count() == 0){

                // crear permisos
                Permiso read = new Permiso(); read.setNombre("READ");
                Permiso post = new Permiso(); post.setNombre("POST");
                Permiso delete = new Permiso(); delete.setNombre("DELETE");

                permisoRepository.saveAll(Set.of(read,post,delete));



                // crear rol

                Rol userRole = new Rol();
                userRole.setNombre("ROLE_USER");
                Set<Permiso> userPermisos = new HashSet<>();
                userPermisos.add(read);
                userPermisos.add(post);
                userRole.setPermisos(userPermisos);


                Rol adminRole = new Rol();
                adminRole.setNombre("ROLE_ADMIN");
                Set<Permiso> adminPermisos = new HashSet<>();
                adminPermisos.add(read);
                adminPermisos.add(post);
                adminPermisos.add(delete);
                adminRole.setPermisos(adminPermisos);




                rolRepository.saveAll(Set.of(userRole,adminRole));
                System.out.println("se cargaron los permisos y roles inicialices en la base de datos");

                List<String> usernames = new ArrayList<>(
                        Arrays.asList("carlos_abarzua","ronald_villalobos","mateon","akomi","vicentito","srbaes","panxito","cristovalsito","paulo","orlangod")
                );

                List<String> emails = new ArrayList<>(
                        Arrays.asList("ca.abarzuac@profesorduoc.cl","ronald@gmail.com","mateito@gmail.com","akmi@yahoo.es","v.garcia@duocuc.cl","r.baes@gmail.com","panxo@outlook.com","ola@hotmail.com","paulo@gmail.com","orlando_sepulveda@duocuc.cl")
                );

                List<String> pwds = new ArrayList<>(
                        Arrays.asList("asd123","asd","ola","asd","123","colocolo","qwerty","paulo","1234","893ss232")
                );

                List<String> bios = new ArrayList<>(
                    Arrays.asList("titi me pregunto","trabajando para usted","soy mateon", "hola mundo", "sr baesssssss","https://github.com/rrrbbb20/proyecto-gimnasio","soy padre","bibliotecovich","asdddd","Oracle DB Engineering")
                );


                for (int i = 0; i < usernames.size(); i++){
                    Usuario usuario = new Usuario();
                    usuario.setUsername(usernames.get(i));
                    usuario.setPassword(passwordEncoder.encode(pwds.get(i)));
                    usuario.setEmail(emails.get(i));
                    usuario.setRoles(Collections.singleton(userRole));

                    UsuarioPerfil userPerfil  = new UsuarioPerfil();
                    userPerfil.setBiografia(bios.get(i));
                    userPerfil.setAvatarUrl("/api/media/avatar/default_avatar.png");
                    userPerfil.setUbicacion("Santiago, Chile");

                    userPerfil.setUsuario(usuario);
                    usuario.setPerfil(userPerfil);

                    usuarioRepository.save(usuario);

                }

                System.out.println("se cargaron los registros de usuarios.");


            }
        };

    }

}
