package com.pomostudy.dto.goal;

import com.pomostudy.entity.Goal;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.GoalRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
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
            goal.getTitle(),
            goal.getDescription(),
            goal.getType(),
            goal.getGoalValue(),
            goal.getGoalActual(),
            goal.getEndDate(),
            goal.getActive()
        );
    }

    public Goal toGoal(GoalRequestDTO goalRequestDTO, Long id) {

        Goal goalUpdateOrCreate;

        if (id != null) {
            goalUpdateOrCreate = goalRepository.findById(id)
                    .filter(goal -> goal.getUserGoal().getId().equals(goalRequestDTO.user_goal()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        } else {
            goalUpdateOrCreate = new Goal();
        }


        Optional<User> user = userRepository.findById(goalRequestDTO.user_goal());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", goalRequestDTO.user_goal());


        goalUpdateOrCreate.setTitle(goalRequestDTO.title());
        goalUpdateOrCreate.setDescription(goalRequestDTO.description());
        goalUpdateOrCreate.setType(goalRequestDTO.type());
        goalUpdateOrCreate.setGoalValue(goalRequestDTO.goalValue());
        goalUpdateOrCreate.setGoalActual(goalRequestDTO.goalActual());
        goalUpdateOrCreate.setEndDate(goalRequestDTO.endDate());
        goalUpdateOrCreate.setActive(goalRequestDTO.active());
        goalUpdateOrCreate.setUserGoal(user.get());

        goalUpdateOrCreate.setCreatedAt(OffsetDateTime.now());

        return goalUpdateOrCreate;
    }
}
