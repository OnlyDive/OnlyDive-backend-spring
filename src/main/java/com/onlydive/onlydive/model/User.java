package com.onlydive.onlydive.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private LicenceEnum licence;
    @Lob
    private String bio;
    @Column(unique = true, nullable = false)
    @Email
    private String email;
    private String password;
    private Instant created;
    private boolean active;
}
