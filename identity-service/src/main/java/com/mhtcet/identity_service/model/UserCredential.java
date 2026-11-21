package com.mhtcet.identity_service.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Stored as BCrypt hash, never plain text

    @Column(nullable = false)
    private String role; // "STUDENT" or "ADMIN"
}
