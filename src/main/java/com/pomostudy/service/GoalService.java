package com.pomostudy.service;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.entity.Task;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.mapper.GoalMapper;
import com.pomostudy.dto.goal.GoalRequestDTO;
import com.pomostudy.dto.goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.repository.GoalRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalMapper goalMapper;
    private final AuthorizationService authorizationService;

    private static final String GOAL = "Goal";

    public GoalService(
            GoalRepository goalRepository,
            UserRepository userRepository,
            GoalMapper goalMapper,
            AuthorizationService authorizationService

    ) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
        this.authorizationService = authorizationService;
    }

    public GoalResponseDTO save(GoalRequestDTO goalRequestDTO, AuthenticatedUser authenticatedUser) {
        Goal goal = goalRepository.save(goalMapper.toCreateGoal(goalRequestDTO, authenticatedUser));
        return goalMapper.toGoalResponseDTO(goal);
    }

    public GoalResponseDTO edit(GoalRequestDTO goalRequestDTO, AuthenticatedUser authenticatedUser, Long id) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Goal.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(GOAL, id);
        }

        Goal goal = goalRepository.save(goalMapper.toUpdateGoal(goalRequestDTO, authenticatedUser, id));
        return goalMapper.toGoalResponseDTO(goal);

    }

    public Page<GoalResponseDTO> findAll(Pageable pageable, AuthenticatedUser authenticatedUser) {
        Page<Goal> goalPage;

        if (authenticatedUser.isAdmin()) {
            goalPage = goalRepository.findAll(pageable);
        } else {
            goalPage = goalRepository.findByUser(authenticatedUser.getUser(), pageable);
        }

        return goalPage.map(goalMapper::toGoalResponseDTO);
    }

    public GoalResponseDTO findById(Long id, AuthenticatedUser authenticatedUser) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Goal.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(GOAL, id);
        }

        return goalRepository.findById(id)
                .map(GoalResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound(GOAL, id));
    }

    public void delete(Long id, AuthenticatedUser authenticatedUser) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Goal.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(GOAL, id);
        }

        goalRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound(GOAL, id));
        goalRepository.deleteById(id);
    }
}
