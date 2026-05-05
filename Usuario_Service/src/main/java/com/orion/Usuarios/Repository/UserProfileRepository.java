package com.orion.Usuarios.Repository;

import com.orion.Usuarios.Entity.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UsuarioPerfil,Long> {
    Optional<UsuarioPerfil> findByUsuarioId(Long usuarioId);

}
