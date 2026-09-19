package com.pomostudy.mapper;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserMapper(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO (
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User toCreateUser(UserCreateRequestDTO userCreateRequestDTO) {

        userRepository.findUserByEmail(userCreateRequestDTO.getEmail()).ifPresent(user -> {
            throw new ResourceException("", "", "EMAIL_DUPLICATED", "Email Already in use");
        });

        String encryptedPassword = passwordEncoder.encode(userCreateRequestDTO.password());

        return new User(
                userCreateRequestDTO.name(),
                userCreateRequestDTO.email(),
                encryptedPassword,
                UserRole.USER
        );
    }

    public User toUpdateUser(UserUpdateRequestDTO userUpdateRequestDTO, AuthenticatedUser authenticatedUser,Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));

        if (!authenticatedUser.getUser().getEmail().equals(user.getEmail()) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound("User", id);
        }

        if (userUpdateRequestDTO.getName() != null) {
            user.setName(userUpdateRequestDTO.getName());
        }

        if (userUpdateRequestDTO.getPassword() != null) {
            String encryptedPassword = passwordEncoder.encode(userUpdateRequestDTO.getPassword());
            user.setPassword(encryptedPassword);
        }

//        user.setUpdatedAt(OffsetDateTime.now());

        return user;
    }
}
