package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
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
    public UserResponseDto create(UserRequestDto userRequestDto) throws InvalidArgumentException {
        try {
            User user = userRepository.save(UserMapper.userFromUserRequestDto(userRequestDto));

            return UserMapper.userToUserResponseDto(user);
        } catch (RuntimeException ex) {
            throw new ConflictException("User with email " + userRequestDto.getEmail() + " already exists");
        }
    }

    @Override
    public UserResponseDto update(UserRequestDto userDtoRequest, Long userId) throws InvalidArgumentException {
        User userToUpdate = findUserIfExists(userId);

        if (userDtoRequest.getName() != null)
            userToUpdate.setName(userDtoRequest.getName());

        if (userDtoRequest.getEmail() != null)
            userToUpdate.setEmail(userDtoRequest.getEmail());

        return UserMapper.userToUserResponseDto(userRepository.save(userToUpdate));
    }

    @Override
    public UserResponseDto get(Long userId) throws NotFoundException {
        User user = findUserIfExists(userId);
        return UserMapper.userToUserResponseDto(user);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::userToUserResponseDto)
                .collect(Collectors.toList());
    }

    private User findUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with  id = " + userId + " not found."));
    }
}