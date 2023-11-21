package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    protected static Long id = 0L;

    @Override
    public User creatUser(UserRequestDto userDtoRequest) throws ConflictException {
        emailCheck(userDtoRequest);
        User user = UserMapper.userFromUserRequestDto(userDtoRequest);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(UserRequestDto userDtoRequest, Long userId) throws ConflictException {
        if (!users.get(userId).getEmail().equals(userDtoRequest.getEmail())) {
            emailCheck(userDtoRequest);
        }
        User user = users.get(userId);
        if (Objects.nonNull(userDtoRequest.getName())) {
            user.setName(userDtoRequest.getName());
        }
        if (Objects.nonNull(userDtoRequest.getEmail())) {
            user.setEmail(userDtoRequest.getEmail());
        }

        users.put(userId, user);
        return user;
    }

    @Override
    public User getUser(Long userId) throws NotFoundException {
        User user = users.get(userId);
        if (Objects.nonNull(user)) {
            return user;
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void emailCheck(UserRequestDto userDtoRequest) throws ConflictException {
        for (User u : users.values()) {
            if (Objects.equals(userDtoRequest.getEmail(), u.getEmail())) {
                throw new ConflictException("Пользователь с таким email уже существует");
            }
        }
    }

    protected static Long generateId() {
        return ++ id;
    }
}
