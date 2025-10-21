package com.pomostudy.entity;

import com.pomostudy.entity.base.UserOwned;
import com.pomostudy.enums.StatusTask;
import com.pomostudy.enums.TaskPriority;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task implements UserOwned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTask status;

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
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "task")
    private Set<PomodoroSession> sessions;

    /*** Constructors ***/

    public Task() {}

    /*** GETTERS AND SETTERS ***/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Integer getTimeTotalLearning() {
        return timeTotalLearning;
    }

    public void setTimeTotalLearning(Integer timeTotalLearning) {
        this.timeTotalLearning = timeTotalLearning;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /*** Methods ***/

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", priority=" + priority +
                ", timeTotalLearning=" + timeTotalLearning +
                ", createdAt=" + createdAt +
                ", userTask=" + user +
                ", category=" + category +
                ", sessions=" + sessions +
                '}';
    }

    @Override
    public User getUser() {
        return user;
    }

    /**
     *
     * Default values when create without the field
     */
    @PrePersist
    protected void onCreate() {
        if (this.startDate == null) {
            this.startDate = OffsetDateTime.now();
        }

        if (this.status == null) {
            this.status = StatusTask.IN_PROGRESS;
        }

        if (this.priority == null) {
            this.priority = TaskPriority.LOW;
        }

        if (this.timeTotalLearning == null) {
            this.timeTotalLearning = 0;
        }
    }
}
