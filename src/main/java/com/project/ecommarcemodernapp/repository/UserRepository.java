package com.project.ecommarcemodernapp.repository;

import com.project.ecommarcemodernapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmailAndIdNot(String email, Long id);

    Optional<Users> findByUsernameAndIdNot(String username, Long id);
}
