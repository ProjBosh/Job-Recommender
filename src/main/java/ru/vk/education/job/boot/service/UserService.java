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
        return null;
//        return userRepository.findAll().stream()
//                .sorted(Comparator.comparing(User::getFirstName))
//                .toList();
    }

    public boolean isDuplicate(String firstName, Set<String> skills, int experience) {
        return false;
//        return userRepository.existsByField(firstName);
    }

    public User getUser(Long id) {
        return null;
//        return userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id + " не найден"));
    }

    public User create(String firstName, Set<String> skills, int experience) {
        return null;
//        User user = new User(null, firstName, skills, experience);
//        return userRepository.save(user);
    }
}
