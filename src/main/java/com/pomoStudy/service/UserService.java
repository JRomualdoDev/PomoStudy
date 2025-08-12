package com.pomoStudy.service;

import com.pomoStudy.entity.User;
import com.pomoStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        try {
            userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Erro ao salvar o usuário.");
        }
    }

    public void edit(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User userUpdate = user.get();
            userUpdate.setName("carlos");
            userUpdate.setEmail("Carlos@hotmail.com");
            userUpdate.setPassword("123");
            userUpdate.setUpdatedAt(OffsetDateTime.now());
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }
}
