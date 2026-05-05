package com.posteos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "post", indexes = {
        @Index(name = "idx_post_userid", columnList = "userid")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(length = 255)
    private String mediaUrl;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creadoEl;
}