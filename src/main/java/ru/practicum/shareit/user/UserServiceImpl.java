package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User creatUser(UserRequestDto userDtoRequest) throws InvalidArgumentException {
        return userRepository.creatUser(userDtoRequest);
    }

    @Override
    public User update(UserRequestDto userDtoRequest, Long userId) throws InvalidArgumentException {
        return userRepository.update(userDtoRequest, userId);
    }

    @Override
    public User getUser(Long userId) throws NotFoundException {
        return userRepository.getUser(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}