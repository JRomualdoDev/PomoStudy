package com.pomostudy.service;

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

    final GoalRepository goalRepository;
    final UserRepository userRepository;
    final GoalMapper goalMapper;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
    }

    public GoalResponseDTO save(GoalRequestDTO goalRequestDTO) {
        Goal goal = goalRepository.save(goalMapper.toCreateGoal(goalRequestDTO));
        return goalMapper.toGoalResponseDTO(goal);
    }

    public GoalResponseDTO edit(GoalRequestDTO goalRequestDTO, Long id) {
        Goal goal = goalRepository.save(goalMapper.toUpdateGoal(goalRequestDTO, id));
        return goalMapper.toGoalResponseDTO(goal);

    }

    public Page<GoalResponseDTO> findAll(Pageable pageable) {
        Page<Goal> goalPage = goalRepository.findAll(pageable);
        return goalPage.map(goalMapper::toGoalResponseDTO);
    }

    public GoalResponseDTO findById(Long id) {
        return goalRepository.findById(id)
                .map(GoalResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
    }

    public void delete(Long id) {
        goalRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        goalRepository.deleteById(id);
    }
}
