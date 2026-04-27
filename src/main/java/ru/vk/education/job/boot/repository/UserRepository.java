package ru.vk.education.job.boot.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.vk.education.job.boot.domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Getter
@RequiredArgsConstructor
public class UserRepository {
    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public boolean existsByField(String firstName, Set<String> skills, int experience) {
        return storage.values().stream()
                .anyMatch(user -> user.getFirstName().equals(firstName));
    }

    public boolean isPresent(Long id) {
        return storage.containsKey(id);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        storage.put(user.getId(), user);
        return user;
    }
}
