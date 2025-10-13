package com.pomostudy.repository;

import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUser(User user, Pageable pageable);

    Page<Task> findByCategoryId(Long categoryId, Pageable pageable);
}
