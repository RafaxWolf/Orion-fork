package com.orion.eventos_service.Repository;

import com.orion.eventos_service.Entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryEvento extends JpaRepository< Evento,Long> {
    List<Evento> findByIdCreador(Long idCreador);

}
