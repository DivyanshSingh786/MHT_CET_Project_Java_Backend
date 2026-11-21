package com.mhtcet.identity_service.repository;

import com.mhtcet.identity_service.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCredential, String> {
    Optional<UserCredential> findByEmail(String email);
}
