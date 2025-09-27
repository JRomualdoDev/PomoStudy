package com.pomostudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.goal.GoalRequestDTO;
import com.pomostudy.dto.goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.entity.User;
import com.pomostudy.enums.GoalType;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import com.pomostudy.service.GoalService;
import com.pomostudy.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GoalController.class)
@Import(SecurityConfigurations.class)
@ActiveProfiles("test")
@WithMockUser(roles = "USER")
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    private GoalRequestDTO goalRequestDTO;
    private GoalResponseDTO goalResponseDTO;
    private Goal goal;
    private final Long goalID = 1L;

    OffsetDateTime endDate = OffsetDateTime.now().plusDays(4L);

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        goal = new Goal();
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
                true,
                1L
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
    @DisplayName("Should be create goal and return status 201 created")
    void shouldCreateGoalReturnStatus201() throws Exception {

        when(goalService.save(any(GoalRequestDTO.class))).thenReturn(goalResponseDTO);

        mockMvc.perform(post("/api/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("api/goal/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Goal test")));
    }

    @Test
    @DisplayName("Should be return 400 Bad request when create a new goal with invalid data")
    void shouldBeReturn400BadRequestWhenCreateNewGoalWithInvalidData() throws Exception {

        GoalRequestDTO invalidGoalRequestDTO = new GoalRequestDTO(
                null,
                "Goal test description",
                GoalType.POMODORO_DAILY,
                2,
                1,
                endDate,
                true,
                1L
        );

        mockMvc.perform(post("/api/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGoalRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be edit goal and return status 200 ok")
    void shouldEditGoalAndReturnStatus200OK() throws Exception {

        when(goalService.edit(any(GoalRequestDTO.class), anyLong())).thenReturn(goalResponseDTO);

        mockMvc.perform(put("/api/goal/{id}", goalID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Goal test"));
    }

    @Test
    @DisplayName("Should be return 404 Not Found for try edit non-existent goal")
    void shouldReturn404TryEditNonExistentGoal() throws Exception {

        when(goalService.edit(any(GoalRequestDTO.class), anyLong()))
                .thenThrow(ResourceExceptionFactory.notFound("Goal", goalID));

        mockMvc.perform(put("/api/goal/{id}", goalID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should be return all Goal with status 200 OK")
    void shouldReturnAllGoalWithStatus200() throws Exception {

        Pageable pageable = PageRequest.of(0,10);
        Page<GoalResponseDTO> goalPage = new PageImpl<>(List.of(goalResponseDTO), pageable, 1);

        when(goalService.findAll(any(Pageable.class))).thenReturn(goalPage);

        mockMvc.perform(get("/api/goal?page=0&size=10&sort=id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Goal test")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

    }

    @Test
    @DisplayName("Should be found one goal for the id and return 200 OK")
    void shouldFoundGoalForTheIdReturn200() throws Exception {

        when(goalService.findById(anyLong())).thenReturn(goalResponseDTO);

        mockMvc.perform(get("/api/goal/{id}", goalID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Goal test"))
                .andExpect(jsonPath("$.description").value("Goal test description"))
                .andExpect(jsonPath("$.type").value("POMODORO_DAILY"))
                .andExpect(jsonPath("$.goalValue").value(2))
                .andExpect(jsonPath("$.goalActual").value(1))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should be delete with success and return status 204 No Content")
    void shouldBeDeleteWithSuccessReturnStatus204() throws Exception {

        when(goalService.findById(anyLong())).thenReturn(goalResponseDTO);
        doNothing().when(goalService).delete(anyLong());

        mockMvc.perform(delete("/api/goal/{id}", goalID))
                .andExpect(status().isNoContent());

        verify(goalService, times(1)).delete(anyLong());
    }
}