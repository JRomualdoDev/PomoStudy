package com.pomostudy.mapper;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.goal.GoalRequestDTO;
import com.pomostudy.dto.goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.GoalRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GoalMapper {

    UserRepository userRepository;
    GoalRepository goalRepository;

    public GoalMapper(UserRepository userRepository, GoalRepository goalRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
    }

    public GoalResponseDTO toGoalResponseDTO(Goal goal) {
        return new GoalResponseDTO(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getType(),
                goal.getGoalValue(),
                goal.getGoalActual(),
                goal.getEndDate(),
                goal.getActive()
        );
    }

    public Goal toCreateGoal(GoalRequestDTO goalRequestDTO, AuthenticatedUser authenticatedUser) {

        Goal goal = new Goal();

        Optional<User> user = userRepository.findById(authenticatedUser.getUser().getId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", authenticatedUser.getUser().getId());

        goal.setTitle(goalRequestDTO.title());
        goal.setDescription(goalRequestDTO.description());
        goal.setType(goalRequestDTO.type());
        goal.setGoalValue(goalRequestDTO.goalValue());
        goal.setGoalActual(goalRequestDTO.goalActual());
        goal.setEndDate(goalRequestDTO.endDate());
        goal.setActive(goalRequestDTO.active());
        goal.setUserGoal(user.get());

        return goal;
    }

    public Goal toUpdateGoal(GoalRequestDTO goalRequestDTO, AuthenticatedUser authenticatedUser, Long id) {

        Goal goal = goalRepository.findById(id)
                    .filter(g -> g.getUserGoal().getId().equals(authenticatedUser.getUser().getId()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));

        Optional<User> user = userRepository.findById(authenticatedUser.getUser().getId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", authenticatedUser.getUser().getId());

        goal.setTitle(goalRequestDTO.title());
        goal.setDescription(goalRequestDTO.description());
        goal.setType(goalRequestDTO.type());
        goal.setGoalValue(goalRequestDTO.goalValue());
        goal.setGoalActual(goalRequestDTO.goalActual());
        goal.setEndDate(goalRequestDTO.endDate());
        goal.setActive(goalRequestDTO.active());
        goal.setUserGoal(user.get());

        return goal;
    }
}
