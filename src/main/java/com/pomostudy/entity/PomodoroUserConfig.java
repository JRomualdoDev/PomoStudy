package com.pomostudy.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Entity
public class PomodoroUserConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int focus_duration;
    private int short_break_duration;
    private int long_break_duration;
    private int cycles_until_long_break;
    private Boolean auto_start_breaks;
    private Boolean auto_start_next_pomodoro;
    private Boolean notification_sound_active;
    private String sound_type;

    private Integer notificationVolume = 50;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user_pomo_config;

    public User getUserPomoConfig() {
        return user_pomo_config;
    }

    public void setUserPomoConfig(User user_pomo_config) {
        this.user_pomo_config = user_pomo_config;
    }
}
