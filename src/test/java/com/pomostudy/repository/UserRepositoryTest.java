package com.pomostudy.repository;

import com.pomostudy.dto.user.UserRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("Should get User sucessfully from db for the email")
    void findUserByEmailCaseSuccess() {
         // 1 - Arrange
        String email = "jromualdo3@hotmail.com";

        // Persist memory db
        UserRequestDTO data =  new UserRequestDTO("junior", email, "A2314@fdaf", UserRole.ADMIN);
        this.createUser(data);

        // 2 - Act
        Optional<UserDetails> result = Optional.ofNullable(this.userRepository.findUserByEmail(email));



        // 3 - Assert
        assertNotNull(result);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should NOT get User from DB when user not exists")
    void findUserByEmailCaseEmpty() {

        String email = "jromualdo3@hotmail.com";

        Optional<UserDetails> result = Optional.ofNullable(this.userRepository.findUserByEmail(email));

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserRequestDTO userRequestDTO) {
        User newUser = new User(
                userRequestDTO.getName(),
                userRequestDTO.getEmail(),
                userRequestDTO.getPassword(),
                userRequestDTO.getRole()
        );
        this.entityManager.persist(newUser);
        return newUser;
    }
}