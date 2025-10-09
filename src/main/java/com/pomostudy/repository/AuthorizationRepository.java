package com.pomostudy.repository;

import com.pomostudy.entity.base.UserOwned;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorizationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public <T extends UserOwned> boolean isOwner(Class<T> entityClass, Long entityId, Long userId) {
        if ( entityId == null || userId == null ) {
            return false;
        }

        // Generic JPQL that uses entity class name dynamically
        String jpql = "SELECT COUNT(e) > 0 FROM " + entityClass.getSimpleName() + " e WHERE e.id = :entityId AND e.user.id = :userId";

        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
        query.setParameter("entityId", entityId);
        query.setParameter("userId", userId);

        return query.getSingleResult();
    }
}
