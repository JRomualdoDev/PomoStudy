package com.pomoStudy.entity;

import com.pomoStudy.enums.PomodoroSessionType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;

@Entity
public class Pomodoro_session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime startDate;

    @Column(nullable = false)
    @CreatedDate
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
