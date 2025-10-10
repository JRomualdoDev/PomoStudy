package com.pomostudy.repository;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByUser(User user, Pageable pageable);

    Optional<Category> findByIdAndUser(Long categoryId, User user);
}
