package com.pomoStudy.service;

import com.pomoStudy.dto.user.UserMapper;
import com.pomoStudy.dto.user.UserRequestDTO;
import com.pomoStudy.dto.user.UserResponseDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceException;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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

        try {
            User user = userMapper.toUser(userRequestDTO, null);

            return userMapper.userResponseDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Error create user.");
        }
    }

    public UserResponseDTO edit(UserRequestDTO userRequestDTO, Long id) {

        try {
            User userUpdate = userMapper.toUser(userRequestDTO, id);

            return userMapper.userResponseDTO(userRepository.save(userUpdate));
        } catch (ResourceException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error updated User");
        }
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
