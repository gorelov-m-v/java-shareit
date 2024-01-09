package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void initUserService() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void findUserById_Should_Throw_NotFoundException_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> userService.get(1L));
        assertThat(result.getMessage()).isEqualTo("User with  id = 1 not found.");
    }

    @Test
    void findUser_PositiveTest() {
        User user = createUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserResponseDto result = userService.get(1L);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(user.getId()),
                () -> assertThat(result.getName()).isEqualTo(user.getName()),
                () -> assertThat(result.getEmail()).isEqualTo(user.getEmail()));
    }

    @Test
    void createUser_Should_Сall_Save_Once_Test() {
        when(userRepository.save(any())).thenReturn(new User());

        userService.create(new UserRequestDto(null, "testUser", "test@email.com"));

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void update_NonExistent_User_Should_Throw_NotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> userService.update(
                        new UserRequestDto(null, "testUser", "test@email.com"), 1L));

        assertThat(result.getMessage()).isEqualTo("User with  id = 1 not found.");
    }

    @Test
    void updateUser_Positive_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        User user = createUser();
        when(userRepository.save(any())).thenReturn(user);

        UserResponseDto result = userService.update(
                new UserRequestDto(null, "testUser", "test@email.com"), 1L);

        verify(userRepository, times(1)).save(any());
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(user.getName()),
                () -> assertThat(result.getEmail()).isEqualTo(user.getEmail()));
    }

    @Test
    void deleteUserById_Should_Сall_DeleteById_Once_Test() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setEmail("test@email.com");

        return user;
    }
}
