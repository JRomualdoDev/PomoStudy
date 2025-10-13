package com.pomostudy.service;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.entity.Task;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.mapper.UserMapper;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            AuthorizationService authorizationService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO save(UserCreateRequestDTO userCreateRequestDTO) {

        User user = userMapper.toCreateUser(userCreateRequestDTO);

        return userMapper.toUserResponseDTO(userRepository.save(user));

    }

    public UserResponseDTO edit(UserUpdateRequestDTO userUpdateRequestDTO, AuthenticatedUser authenticatedUser, Long id) {

        User userUpdate = userMapper.toUpdateUser(userUpdateRequestDTO, authenticatedUser, id);

        return userMapper.toUserResponseDTO(userRepository.save(userUpdate));

    }

    public Page<UserResponseDTO> findAll(Pageable pageable, AuthenticatedUser authenticatedUser) {
        Page<User> userPage;
        if (authenticatedUser.isAdmin()) {
            userPage = userRepository.findAll(pageable);
        } else {
            Optional<User> user = userRepository.findById(authenticatedUser.getUser().getId());

            if (user.isPresent()) {
                userPage = new PageImpl<User>(Collections.singletonList((user)), pageable, 1);
            }

        }

        return userPage.map(userMapper::toUserResponseDTO);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
