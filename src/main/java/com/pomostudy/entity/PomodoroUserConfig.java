package com.pomostudy.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class PomodoroUserConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int focusDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private int cyclesUntilLongBreak;
    private Boolean autoStartBreaks;
    private Boolean autoStartNextPomodoro;
    private Boolean notificationSoundActive;
    private String soundType;

    private Integer notificationVolume = 50;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @Column(name = "userId", nullable = false, unique = true)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User userPomoConfig;

    public User getUserPomoConfig() {
        return userPomoConfig;
    }

    public void setUserPomoConfig(User userPomoConfig) {
        this.userPomoConfig = userPomoConfig;
    }
}
