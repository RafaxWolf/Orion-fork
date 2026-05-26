package com.orion.chatservice.Repository;


import com.orion.chatservice.Entity.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {


    // findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByFechaEnvioDesc

    Page<Mensaje> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByFechaEnvioDesc(
            Long s1, Long r1, Long s2, Long r2, Pageable pageable
    );


    // para chat global

    Page<Mensaje> findByReceiverIdOrderByFechaEnvioDesc(Long receiverId, Pageable pageable);


}
