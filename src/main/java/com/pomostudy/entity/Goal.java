package com.pomostudy.entity;

import com.pomostudy.enums.GoalType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalType type;

    @Column(nullable = false)
    private Integer goalValue;

    @Column(nullable = false)
    private Integer goalActual;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    private OffsetDateTime endDate;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userGoal;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GoalHistory> goalHistory = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GoalType getType() {
        return type;
    }

    public void setType(GoalType type) {
        this.type = type;
    }

    public Integer getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(Integer goalValue) {
        this.goalValue = goalValue;
    }

    public Integer getGoalActual() {
        return goalActual;
    }

    public void setGoalActual(Integer goalActual) {
        this.goalActual = goalActual;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUserGoal() {
        return userGoal;
    }

    public void setUserGoal(User userGoal) {
        this.userGoal = userGoal;
    }

    public List<GoalHistory> getGoalHistory() {
        return goalHistory;
    }

    public void setGoalHistory(List<GoalHistory> goalHistory) {
        this.goalHistory = goalHistory;
    }
}
