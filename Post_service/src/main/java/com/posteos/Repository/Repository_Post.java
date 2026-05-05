package com.posteos.Repository;

import com.posteos.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repository_Post extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
