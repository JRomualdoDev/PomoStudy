package com.pomoStudy.service;

import com.pomoStudy.dto.UserRequestDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(UserRequestDTO userRequestDTO) {
        try {
            User user = new User();
            user.setName(userRequestDTO.getName());
            user.setEmail(userRequestDTO.getEmail());
            user.setPassword(userRequestDTO.getPassword());

            userRequestDTO.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Erro ao salvar o usuário.");
        }
    }

    public void edit(UserRequestDTO userRequestDTO, Long id) {
        User userUpdate = userRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));

        if (userRequestDTO.getName() == null) {
            userUpdate.setName(userRequestDTO.getName());
            userUpdate.setEmail(userRequestDTO.getEmail());
            userUpdate.setPassword(userRequestDTO.getPassword());
            userUpdate.setUpdatedAt(OffsetDateTime.now());

            userRepository.save(userUpdate);
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }
}
