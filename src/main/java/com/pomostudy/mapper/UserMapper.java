package com.pomostudy.mapper;

import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
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

    public User toCreateUser(UserCreateRequestDTO userCreateRequestDTO) {

            UserDetails userFound = userRepository.findUserByEmail(userCreateRequestDTO.getEmail());
            Optional.ofNullable(userFound)
                    .ifPresent(userDetails -> {
                        throw new ResourceException(
                                "",
                                "",
                                "EMAIL DUPLICATED",
                                "Email Already in use"
                        );
                    });

            String encryptedPassword = new BCryptPasswordEncoder().encode(userCreateRequestDTO.password());

            return new User(
                    userCreateRequestDTO.name(),
                    userCreateRequestDTO.email(),
                    encryptedPassword,
                    UserRole.USER
            );
    }

    public User toUpdateUser(UserUpdateRequestDTO userUpdateRequestDTO, Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));

        if (userUpdateRequestDTO.getName() != null) {
            user.setName(userUpdateRequestDTO.getName());
        }

        if (userUpdateRequestDTO.getPassword() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        user.setUpdatedAt(OffsetDateTime.now());

        return user;
    }
}
