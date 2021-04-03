package com.example.jwttuto.repository;

import com.example.jwttuto.entity.User1;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface User1Repository extends JpaRepository<User1, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User1> findOneWithAuthoritiesByUsername(String username);
}
