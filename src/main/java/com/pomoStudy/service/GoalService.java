package com.pomoStudy.service;

import com.pomoStudy.dto.Goal.GoalMapper;
import com.pomoStudy.dto.Goal.GoalRequestDTO;
import com.pomoStudy.dto.Goal.GoalResponseDTO;
import com.pomoStudy.entity.Goal;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceException;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.GoalRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {

    final GoalRepository goalRepository;
    final UserRepository userRepository;
    final GoalMapper goalMapper;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
    }

    public GoalResponseDTO save(GoalRequestDTO goalRequestDTO) {

        try {
            Goal goal = goalRepository.save(goalMapper.toGoal(goalRequestDTO, null));

            return goalMapper.toGoalResponseDTO(goal);

        } catch(ResourceException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Error create Goal.");
        }
    }

    public GoalResponseDTO edit(GoalRequestDTO goalRequestDTO, Long id) {

        try {
            Goal goal = goalRepository.save(goalMapper.toGoal(goalRequestDTO, id));

            return goalMapper.toGoalResponseDTO(goal);

        } catch (ResourceException e) {
            System.out.println(e.getMessage());
            throw e;
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
