package com.project.ecommarcemodernapp.repository;

import com.project.ecommarcemodernapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUsersByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Long id(Long id);
}
