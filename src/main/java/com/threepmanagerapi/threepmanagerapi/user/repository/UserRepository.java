package com.threepmanagerapi.threepmanagerapi.user.repository;

import com.threepmanagerapi.threepmanagerapi.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByUserID(Long id);
}
