package com.onlydive.onlydive.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<SpotComment> comments;
    @ManyToOne(cascade = CascadeType.ALL)
    private User creator;
    private Instant creationDate;
    //todo pogoda

}
