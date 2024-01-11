package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void saveUsers() {
        user = createUser();
    }

    @Test
    void findAllUsers_Positive_Test() {
        List<UserResponseDto> resultUsers = userService.getAll();
        UserResponseDto result = resultUsers.get(0);

        assertAll(
                () -> assertEquals(resultUsers.size(), 1),
                () -> assertThat(result.getName()).isEqualTo(user.getName()),
                () -> assertThat(result.getEmail()).isEqualTo(user.getEmail()));
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setEmail("test@email.com");
        userRepository.save(user);
        return user;
    }
}