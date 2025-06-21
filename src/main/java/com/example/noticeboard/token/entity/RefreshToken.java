package com.example.noticeboard.token.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {
    @Id
    private String userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

}
