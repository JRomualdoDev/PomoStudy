package com.pomostudy.entity;

import com.pomostudy.enums.PomodoroSessionType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class PomodoroSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime startDate;

    @Column(nullable = false)
    private OffsetDateTime endDate;

    private Integer plannedDuration;

    private Integer realDuration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PomodoroSessionType type;

    private Boolean completed;

    private String observations;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
