package com.pomostudy.service;

import com.pomostudy.entity.base.UserOwned;
import com.pomostudy.repository.AuthorizationRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthorizationRepository authorizationRepository;

    public AuthorizationService(AuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public <T extends UserOwned> boolean isOwner(Class<T> entityClass, Long entityId, Long userId) {
        return authorizationRepository.isOwner(entityClass, entityId, userId);
    }
}
