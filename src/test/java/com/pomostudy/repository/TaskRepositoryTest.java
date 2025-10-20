package com.pomostudy.repository;

import com.pomostudy.entity.Category;
import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import com.pomostudy.enums.StatusTask;
import com.pomostudy.enums.TaskPriority;
import com.pomostudy.enums.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EntityManager entityManager;

    @TestConfiguration
    static class JpaTestConfig {

        @Bean(name = "auditingDateTimeProvider")
        public DateTimeProvider auditingDateTimeProvider() {
            // Return the provider that always use UTC
            return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
        }
    }

    @Test
    void shouldFindTasksPerMonth() {

        User user = new User(
                "testUser",
                "test@test.com",
                "Test@1234",
                UserRole.USER
        );
        entityManager.persist(user);

        Category category = new Category(
                "testCategory",
                "#fff",
                "test.png",
                user
        );

        entityManager.persist(category);

        OffsetDateTime startOfMonth = OffsetDateTime.of(2025, 11, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime endOfMonth = OffsetDateTime.of(2025, 12, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        Task task1 = new Task(); // Requer um construtor padrão new Task()
        task1.setName("testTask1");
        task1.setDescription("test for task1");
        task1.setStartDate(startOfMonth.plusDays(1));// (02/Nov)
        task1.setEndDate(endOfMonth.plusDays(1));
        task1.setStatus(StatusTask.IN_PROGRESS);
        task1.setPriority(TaskPriority.MEDIUM);
        task1.setTimeTotalLearning(2);
        task1.setUser(user);
        task1.setCategory(category);

        Task task2 = new Task();
        task2.setName("testTask2");
        task2.setDescription("test for task2");
        task2.setStartDate(startOfMonth.plusDays(15)); // (16/Nov)
        task2.setEndDate(endOfMonth.plusDays(1));
        task2.setStatus(StatusTask.IN_PROGRESS);
        task2.setPriority(TaskPriority.MEDIUM);
        task2.setTimeTotalLearning(2);
        task2.setUser(user);
        task2.setCategory(category);

        // Interval between interval 02/nov and 16/nov
        entityManager.persist(task1);
        entityManager.persist(task2);

        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> resultPage = taskRepository.findAllTaskByMonth(
                user.getId(),
                startOfMonth,
                endOfMonth,
                pageable
        );

        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent())
                .extracting(Task::getId)
                .containsExactlyInAnyOrder(task1.getId(), task2.getId());
    }
}