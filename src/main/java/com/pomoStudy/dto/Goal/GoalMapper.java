package com.pomoStudy.dto.Goal;

import com.pomoStudy.entity.Goal;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.GoalRepository;
import com.pomoStudy.repository.UserRepository;
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
                    .filter((goal) -> goal.getUser_goal().getId().equals(goalRequestDTO.user_goal()))
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
        goalUpdateOrCreate.setUser_goal(user.get());

        goalUpdateOrCreate.setCreatedAt(OffsetDateTime.now());

        return goalUpdateOrCreate;
    }
}
