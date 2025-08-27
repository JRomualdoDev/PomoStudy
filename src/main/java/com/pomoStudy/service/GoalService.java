package com.pomoStudy.service;

import com.pomoStudy.dto.Goal.GoalRequestDTO;
import com.pomoStudy.entity.Goal;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.GoalRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {

    final GoalRepository goalRepository;
    final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public void save(GoalRequestDTO goalRequestDTO) {

        Optional<User> user = userRepository.findById(goalRequestDTO.user_goal());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", goalRequestDTO.user_goal());

        try {
            Goal goal = new Goal();
            goal.setTitle(goalRequestDTO.title());
            goal.setDescription(goalRequestDTO.description());
            goal.setType(goalRequestDTO.type());
            goal.setGoalValue(goalRequestDTO.goalValue());
            goal.setGoalActual(goalRequestDTO.goalActual());
            goal.setEndDate(goalRequestDTO.endDate());
            goal.setActive(goalRequestDTO.active());
            goal.setUser_goal(user.get());

            goal.setCreatedAt(OffsetDateTime.now());

            goalRepository.save(goal);

        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Error create Goal.");
        }
    }

    public void edit(GoalRequestDTO goalRequestDTO, Long id) {

        Optional<User> user = userRepository.findById(goalRequestDTO.user_goal());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", goalRequestDTO.user_goal());

        Goal goalUpdate = goalRepository.findById(id)
                .filter((goal) -> goal.getId().equals(goalRequestDTO.user_goal()))
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));

        try {
            goalUpdate.setTitle(goalRequestDTO.title());
            goalUpdate.setDescription(goalUpdate.getDescription());
            goalUpdate.setType(goalRequestDTO.type());
            goalUpdate.setGoalValue(goalRequestDTO.goalValue());
            goalUpdate.setGoalActual(goalRequestDTO.goalActual());
            goalUpdate.setEndDate(goalRequestDTO.endDate());
            goalUpdate.setActive(goalRequestDTO.active());

            goalRepository.save(goalUpdate);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error updated goal.");
        }
    }

    public List<Goal> findAll() {
        return goalRepository.findAll();
    }

    public Optional<Goal> findById(Long id) {
        return goalRepository.findById(id);
    }

    public void delete(Long id) {
        goalRepository.deleteById(id);
    }
}
