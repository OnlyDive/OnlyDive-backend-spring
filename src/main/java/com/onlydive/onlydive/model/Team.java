package com.onlydive.onlydive.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private User owner;
    private Integer membersLimit;
    private Float averageMemberLicence;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> members;
    private Instant lastActivity;
    private Instant eventDate;
}
