package com.pomostudy.service;

import com.pomostudy.dto.user.UserMapper;
import com.pomostudy.dto.user.UserRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
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
    final private UserRepository userRepository;
    final private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {

        User user = userMapper.toUser(userRequestDTO, null);

        return userMapper.toUserResponseDTO(userRepository.save(user));

    }

    public UserResponseDTO edit(UserRequestDTO userRequestDTO, Long id) {

        User userUpdate = userMapper.toUser(userRequestDTO, id);

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
