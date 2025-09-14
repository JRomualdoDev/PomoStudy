package com.pomostudy.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
public class Goal_history {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime referenceDate;

    @Column(nullable = false)
    private Integer registeredValue;

    @Column(nullable = false)
    private BigDecimal percentageAchieved;

    private String observation;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;
}
