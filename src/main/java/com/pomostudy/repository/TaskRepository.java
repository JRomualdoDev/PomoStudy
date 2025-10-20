package com.pomostudy.repository;

import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUser(User user, Pageable pageable);

    Page<Task> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.startDate >= :start AND t.startDate < :end")
    Page<Task> findAllTaskByMonth(@Param("userId") Long userId, @Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end, Pageable pageable);
}
