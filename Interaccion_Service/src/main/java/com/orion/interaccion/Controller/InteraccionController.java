package com.orion.interaccion.Controller;


import com.orion.interaccion.Entity.Follow;
import com.orion.interaccion.Service.FollowService;
import com.orion.interaccion.Service.InteraccionService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interacciones")
public class InteraccionController {

    @Autowired
    private InteraccionService interaccionService;

    @Autowired
    private FollowService followService;


    // boton para dar like a un post
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Boolean> toggleLike(@PathVariable Long postId){

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        boolean isLiked = interaccionService.toggleLike(userId,postId);

        return ResponseEntity.ok(isLiked);
    }


    // boton para obtener un numero con el total de likes
    @GetMapping("/post/{postId}/like/count")
    public ResponseEntity<Long> contarLikes(@PathVariable Long postId){
        return ResponseEntity.ok(interaccionService.obtenerTotalLikes(postId));
    }


    // para verificar si likeaste un post
    @GetMapping("/post/{postId}/like/status")
    public ResponseEntity<Boolean> checkIsLikedByMe(@PathVariable Long postId){

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return ResponseEntity.ok(interaccionService.verificarSiLikeo(userId,postId));

    }


    // Boton para dar follow a un usuario
    @PostMapping("/usuarios/{seguidoId}/follow")
    public ResponseEntity<Boolean> toggleFollow(@PathVariable Long seguidoId){
        // se obtiene el id del usuario autenticado con la sesión
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        boolean isFollowing = followService.toggleFollow(userId,seguidoId);
        return ResponseEntity.ok(isFollowing);
    }



    // Obtener el numero de seguidores que tiene un usuario
    @GetMapping("/usuarios/{usuarioId}/seguidores/count")
    public ResponseEntity<Long> getSeguidoresCount(@PathVariable Long usuarioId){
        return ResponseEntity.ok(followService.obtenerContadorSeguidores(usuarioId));
    }

    // Obtener el numero de personas que sigue un usuario
    @GetMapping("/usuarios/{usuarioId}/seguidos/count")
    public ResponseEntity<Long> getSeguidosCount(@PathVariable Long usuarioId){
        return ResponseEntity.ok(followService.obtenerContadorSeguidos(usuarioId));
    }

    // Para el frontend identificar (true o false) si sigues a esa persona ( para mostrar un boton acorde a si la sigues o no )
    @GetMapping("/usuarios/{seguidoId}/siguiendo")
    public ResponseEntity<Boolean> checkFollowingStatus(@PathVariable Long seguidoId){
        Long seguidorId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return ResponseEntity.ok(followService.verificarSiSigue(seguidorId, seguidoId));
    }



}
