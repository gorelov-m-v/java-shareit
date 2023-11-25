package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto create(UserRequestDto userDtoRequest) throws InvalidArgumentException {
        User user = userRepository.creatUser(userDtoRequest);
        return UserMapper.userToUserResponseDto(user);
    }

    @Override
    public UserResponseDto update(UserRequestDto userDtoRequest, Long userId) throws InvalidArgumentException {
        User user = userRepository.update(userDtoRequest, userId);
        return UserMapper.userToUserResponseDto(user);
    }

    @Override
    public UserResponseDto getUser(Long userId) throws NotFoundException {
        User user = userRepository.getUser(userId);
        return UserMapper.userToUserResponseDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::userToUserResponseDto)
                .collect(Collectors.toList());
    }
}