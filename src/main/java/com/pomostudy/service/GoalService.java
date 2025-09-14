package com.pomostudy.service;

import com.pomostudy.dto.Goal.GoalMapper;
import com.pomostudy.dto.Goal.GoalRequestDTO;
import com.pomostudy.dto.Goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.repository.GoalRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.stereotype.Service;

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
