package ru.semenov.calendar.db.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDbService {
    private final UserRepository repository;

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User save(User user) {
        return repository.save(user);
    }
}
