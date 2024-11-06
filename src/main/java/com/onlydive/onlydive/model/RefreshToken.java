package com.onlydive.onlydive.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Entity
@RequiredArgsConstructor
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
