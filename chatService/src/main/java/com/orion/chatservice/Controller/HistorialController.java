package com.orion.chatservice.Controller;


import com.orion.chatservice.Entity.Mensaje;
import com.orion.chatservice.Repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class HistorialController {


    @Autowired
    private MensajeRepository repo;


    @GetMapping("/historial")
    public Page<Mensaje> obtenerHistorial(
            @RequestParam Long user1,
            @RequestParam Long user2,
            @RequestParam int page,
            @RequestParam int size
    ){


        Pageable pageable = PageRequest.of(page, size);

        return repo.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByFechaEnvioDesc(
                user1,user2,user2,user1,pageable
        );

    }

    @GetMapping("/historial/global")
    public Page<Mensaje> obtenerHistorialGlobal(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return repo.findByReceiverIdOrderByFechaEnvioDesc(0L, PageRequest.of(page, size));

    }



}
