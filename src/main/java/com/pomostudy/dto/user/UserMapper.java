package com.pomostudy.dto.user;

import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
public class UserMapper {

    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO (
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User toUser(UserRequestDTO userRequestDTO, Long id) {

        User userUpdateOrCreate;

        if (id != null) {
            userUpdateOrCreate = userRepository.findById(id)
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));
            userUpdateOrCreate.setUpdatedAt(OffsetDateTime.now());
            userUpdateOrCreate.setName(userRequestDTO.getName());
            userUpdateOrCreate.setEmail(userUpdateOrCreate.getEmail());
            userUpdateOrCreate.setPassword(userUpdateOrCreate.getPassword());
        } else {

            UserDetails userFound = userRepository.findUserByEmail(userRequestDTO.getEmail());
            Optional.ofNullable(userFound)
                    .ifPresent(userDetails -> {
                        throw new ResourceException(
                                "",
                                "",
                                "EMAIL DUPLICATED",
                                "Email Already in use"
                        );
                    });

            String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDTO.password());

            userUpdateOrCreate = new User(
                    userRequestDTO.name(),
                    userRequestDTO.email(),
                    encryptedPassword,
                    userRequestDTO.role()
            );
        }

        return userUpdateOrCreate;
    }
}
