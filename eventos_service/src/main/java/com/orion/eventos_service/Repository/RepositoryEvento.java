package com.orion.eventos_service.Repository;

import com.orion.eventos_service.Entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RepositoryEvento extends JpaRepository< Evento,Long> {
    List<Evento> findByIdCreador(Long idCreador);

}
