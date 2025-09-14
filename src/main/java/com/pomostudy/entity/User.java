package com.pomostudy.entity;

import com.pomostudy.dto.user.UserRequestDTO;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "user_task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "userCategory", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user_goal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Goal> goal = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
        task.setUser(this); // mantém o relacionamento bidirecional consistente
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setUser(null);
    }

    @OneToOne(mappedBy = "user_pomo_config", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PomodoroUserConfig pomodoroUserConfig;

    // Getter e Setter
    public PomodoroUserConfig getPomodoroConfiguration() {
        return pomodoroUserConfig;
    }

    public void setPomodoroConfiguration(PomodoroUserConfig pomodoroConfiguration) {
        this.pomodoroUserConfig = pomodoroConfiguration;
    }

    public User() {

    }

    public User(UserRequestDTO userRequestDTO) {
        this.name = userRequestDTO.getName();
        this.email = userRequestDTO.getEmail();
        this.password = userRequestDTO.getPassword();
        this.createdAt = OffsetDateTime.now();
    }

    /* Setters e Getters */

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
