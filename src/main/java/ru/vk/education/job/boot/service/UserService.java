package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getFirstName))
                .toList();
    }

    public boolean isDuplicate(String firstName, Set<String> skills, int experience) {
        return userRepository.existsByField(firstName);
    }

    public boolean isPresent(User user) {
        if(user == null || user.getId() == null) {
            return false;
        }
        return userRepository.isPresent(user.getId());
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id + " не найден"));
    }

    public User addUser(String firstName, Set<String> skills, int experience) {
        User user = new User(null, firstName, skills, experience);
        return userRepository.save(user);
    }
}
