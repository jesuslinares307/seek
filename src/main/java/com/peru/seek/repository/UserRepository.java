package com.peru.seek.repository;

import com.peru.seek.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //User findByUsername(String username);
    Optional<UserEntity> findByUsername(String username);


}
