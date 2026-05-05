package com.orion.mediaservice.Config;

import com.orion.mediaservice.Entity.Media;
import com.orion.mediaservice.Repository.MediaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(MediaRepository mediaRepository){
        return args -> {

            if(mediaRepository.count() == 0){

                Media media = new Media();
                media.setNombreGenerado("default_avatar.png");
                media.setNombreOriginal("default_avatar.png");
                media.setTipo(Media.TipoMedia.AVATAR);
                media.setUrlAcceso("/api/media/default_avatar.png");
                media.setUserId(null);

                mediaRepository.saveAll(Set.of(media));

            }

        };
    }

}
