package com.pomostudy.dto.user;

import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
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

            // find email in db
            Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(userRequestDTO.getEmail()));

            if (user.isPresent()) throw new ResourceException("", "", "EMAIL DUPLICATED", "Email Already in use" );

            userUpdateOrCreate = new User(userRequestDTO);
        }

        return userUpdateOrCreate;
    }
}
