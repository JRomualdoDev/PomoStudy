package com.pomoStudy.entity;

import com.pomoStudy.enums.StatusUser;
import com.pomoStudy.enums.TaskPriority;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;


@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUser status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Column(nullable = false)
    private Integer timeTotalLearning;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoriaId")
    private Category category;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
