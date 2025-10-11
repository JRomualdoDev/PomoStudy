package com.pomostudy.service;


import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.goal.GoalRequestDTO;
import com.pomostudy.dto.goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.entity.User;
import com.pomostudy.enums.GoalType;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.mapper.GoalMapper;
import com.pomostudy.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalMapper goalMapper;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private GoalService goalService;

    private User user;

    private GoalRequestDTO goalRequestDTO;
    private GoalResponseDTO goalResponseDTO;
    private Goal goal;

    OffsetDateTime endDate = OffsetDateTime.now().plusDays(4L);

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        goal = new Goal();
        goal.setId(1L);
        goal.setTitle("Goal test");
        goal.setDescription("Goal test description");
        goal.setType(GoalType.POMODORO_DAILY);
        goal.setGoalValue(2);
        goal.setGoalActual(1);
        goal.setEndDate(endDate);
        goal.setActive(true);
        goal.setUserGoal(user);

        goalRequestDTO = new GoalRequestDTO(
                "Goal test",
                "Goal test description",
                GoalType.POMODORO_DAILY,
                2,
                1,
                endDate,
                true
        );

        goalResponseDTO = new GoalResponseDTO(
                1L,
                "Goal test",
                "Goal test description",
                GoalType.POMODORO_DAILY,
                2,
                1,
                endDate,
                true
        );
    }

    @Test
    @DisplayName("Should save Goal with success in the db")
    void shouldSaveGoalSuccessfully() {

        when(goalMapper.toCreateGoal(any(GoalRequestDTO.class), any(AuthenticatedUser.class))).thenReturn(goal);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);
        when(goalMapper.toGoalResponseDTO(any(Goal.class))).thenReturn(goalResponseDTO);

        GoalResponseDTO result = goalService.save(goalRequestDTO, authenticatedUser);

        assertNotNull(result);
        assertEquals("Goal test", result.title());
        assertEquals("Goal test description", result.description());
        assertEquals(GoalType.POMODORO_DAILY, result.type());
        assertEquals(2, result.goalValue());
        assertEquals(1, result.goalActual());
        assertEquals(endDate, result.endDate());
        assertEquals(true, result.active());


        verify(goalMapper, times(1)).toCreateGoal(any(GoalRequestDTO.class), any(AuthenticatedUser.class));
        verify(goalRepository, times(1)).save(any(Goal.class));
        verify(goalMapper, times(1)).toGoalResponseDTO(any(Goal.class));
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {

        when(goalMapper.toCreateGoal(any(GoalRequestDTO.class), any(AuthenticatedUser.class))).thenThrow(ResourceExceptionFactory.notFound("User", user.getId()));

        ResourceException error = assertThrows(ResourceException.class, () -> goalService.save(goalRequestDTO, authenticatedUser));
        assertEquals("User with id 1 not found.", error.getMessage());

        verify(goalMapper, times(1)).toCreateGoal(goalRequestDTO, authenticatedUser);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    @DisplayName("Should edit goal successfully in the db")
    void shouldEditGoalSuccessfully() {
        Long goalId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Goal.class, goalId, user.getId())).thenReturn(true);
        when(goalMapper.toUpdateGoal(goalRequestDTO, authenticatedUser, goalId)).thenReturn(goal);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);
        when(goalMapper.toGoalResponseDTO(any(Goal.class))).thenReturn(goalResponseDTO);

        GoalResponseDTO result = goalService.edit(goalRequestDTO, authenticatedUser, goalId);

        assertNotNull(result);
        assertEquals("Goal test", result.title());
        assertEquals("Goal test description", result.description());
        assertEquals(GoalType.POMODORO_DAILY, result.type());
        assertEquals(2, result.goalValue());
        assertEquals(1, result.goalActual());
        assertEquals(endDate, result.endDate());
        assertEquals(true, result.active());

        verify(goalMapper, times(1)).toUpdateGoal(goalRequestDTO, authenticatedUser,goalId);
        verify(goalRepository, times(1)).save(goal);
        verify(goalMapper, times(1)).toGoalResponseDTO(goal);
    }

    @Test
    @DisplayName("Should throw exception when user is not belong to goal")
    void shouldThrowExceptionWhenUserNotBelongToGoal() {

        Long goalId = 1L;

        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Goal.class, goalId, user.getId())).thenReturn(true);
        when(goalMapper.toUpdateGoal(goalRequestDTO, authenticatedUser, goalId)).thenThrow(ResourceExceptionFactory.notFound("Goal", user.getId()));

        ResourceException error = assertThrows(ResourceException.class, () -> goalService.edit(goalRequestDTO, authenticatedUser, goalId));

        assertEquals("Goal with id 1 not found.", error.getMessage());

        verify(goalMapper, times(1)).toUpdateGoal(goalRequestDTO, authenticatedUser, goalId);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    @DisplayName("Should throw exception when goal does not found")
    void shouldThrowExceptionWhenGoalNotFound() {

        Long goalId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Goal.class, goalId, user.getId())).thenReturn(true);
        when(goalMapper.toUpdateGoal(goalRequestDTO, authenticatedUser, goalId)).thenThrow(ResourceExceptionFactory.notFound("Goal", goalId));
        ResourceException error = assertThrows(ResourceException.class, () -> goalService.edit(goalRequestDTO, authenticatedUser, goalId));

        assertEquals("Goal with id 1 not found.", error.getMessage());

        verify(goalMapper, times(1)).toUpdateGoal(goalRequestDTO, authenticatedUser, goalId);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    @DisplayName("Should find all goals for an ADMIN user and return 200 OK")
    void shouldFindAllGoalsForAdmin() {

        List<Goal> goalResponse = Collections.singletonList(goal);
        Pageable pageable = PageRequest.of(0, 10);


        when(authenticatedUser.isAdmin()).thenReturn(true);

        when(goalRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(goalResponse));
        when(goalMapper.toGoalResponseDTO(goal)).thenReturn(goalResponseDTO);


        Page<GoalResponseDTO> result = goalService.findAll(pageable, authenticatedUser);


        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        verify(goalRepository, times(1)).findAll(any(Pageable.class));
        verify(goalRepository, never()).findByUser(any(), any());
    }

    @Test
    @DisplayName("Should find goals for a NON-ADMIN user and return 200 OK")
    void shouldFindAllGoalsDBWithStatus200() {

        List<Goal> goalResponse = Collections.singletonList(goal);
        Pageable pageable = PageRequest.of(0,10);

        when(authenticatedUser.isAdmin()).thenReturn(false);
        when(authenticatedUser.getUser()).thenReturn(user);

        when(goalRepository.findByUser(user, pageable)).thenReturn(new PageImpl<>(goalResponse));
        when(goalMapper.toGoalResponseDTO(goal)).thenReturn(goalResponseDTO);

        Page<GoalResponseDTO> result = goalService.findAll(pageable, authenticatedUser);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(goalResponseDTO,result.getContent().getFirst());
        assertEquals(goalRequestDTO.title(), result.getContent().getFirst().title());
        assertEquals(goalRequestDTO.description(), result.getContent().getFirst().description());
        assertEquals(goalRequestDTO.type(), result.getContent().getFirst().type());

        verify(goalRepository, never()).findAll(pageable);
        verify(goalRepository, times(1)).findByUser(user, pageable);
    }

    @Test
    @DisplayName("Should find the goal from id when the goal exist in the db")
    void shouldFindGoalFromIdWhenGoalExistDB() {
        Long goalId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Goal.class, goalId, user.getId())).thenReturn(true);
        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));

        Optional<GoalResponseDTO> result = Optional.ofNullable(goalService.findById(goalId,authenticatedUser));

        assertTrue(result.isPresent());
        assertEquals(goalResponseDTO, result.get());

        verify(goalRepository, times(1)).findById(goalId);
    }

    @Test
    @DisplayName("Should get Exception when do not find goal from id")
    void shouldGetExceptionWhenNotFindGoalFromId() {
        Long goalId = 99L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Goal.class, goalId, user.getId())).thenReturn(true);
        when(goalRepository.findById(goalId)).thenThrow(ResourceExceptionFactory.notFound("Goal", goalId));

        ResourceException error = assertThrows(ResourceException.class, () -> goalService.findById(goalId, authenticatedUser));

        assertEquals("Goal with id 99 not found.", error.getMessage());

        verify(goalRepository, times(1)).findById(goalId);
    }

    @Test
    @DisplayName("Should delete goal succefully from db")
    void shouldDeleteGoalSuccessfullyDB() {
        Long goalId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Goal.class, goalId, user.getId())).thenReturn(true);
        when(goalRepository.findById(goalId)).thenReturn(Optional.ofNullable(goal));
        doNothing().when(goalRepository).deleteById(goalId);

        goalService.delete(goalId,authenticatedUser);

        verify(goalRepository, times(1)).deleteById(goalId);
    }
}