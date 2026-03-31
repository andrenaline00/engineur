package com.special.repositories;

import com.special.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // gia log in me username
    // Optional<User> findByUsername(String username);
    // boolean existsByUsername(String username);

    // log in me email
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
