package com.pomostudy.service;

import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.mapper.UserMapper;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO save(UserCreateRequestDTO userCreateRequestDTO) {

        User user = userMapper.toCreateUser(userCreateRequestDTO);

        return userMapper.toUserResponseDTO(userRepository.save(user));

    }

    public UserResponseDTO edit(UserUpdateRequestDTO userUpdateRequestDTO, Long id) {

        User userUpdate = userMapper.toUpdateUser(userUpdateRequestDTO, id);

        return userMapper.toUserResponseDTO(userRepository.save(userUpdate));

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
