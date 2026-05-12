package com.orion.interaccion.Service;


import com.orion.interaccion.Entity.Like;
import com.orion.interaccion.Repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InteraccionService {

    @Autowired
    private LikeRepository repo;


    // interruptor de like (Toggle)
    public boolean toggleLike(Long usuarioId, Long postId){

        Optional<Like> likeExistente = repo.findByUsuarioIdAndPostId(usuarioId, postId);


        if (likeExistente.isPresent()) {
            // si ya habia dado like, se le quita
            repo.delete(likeExistente.get());
            return false;
        } else {
            // si no le habia dado like entonces se crea el like
            Like like = new Like();
            like.setUsuarioId(usuarioId);
            like.setPostId(postId);
            repo.save(like);
            return true;
        }
    }


    public long obtenerTotalLikes(Long postId){
        return repo.countByPostId(postId);
    }


    public boolean verificarSiLikeo(Long userId, Long postId){
        return repo.findByUsuarioIdAndPostId(userId, postId).isPresent();
    }


}
