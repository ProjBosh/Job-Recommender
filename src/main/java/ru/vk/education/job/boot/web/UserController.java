package ru.vk.education.job.boot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User request) {
        Set<String> uniqueSkills = new HashSet<>(request.getSkills());

        if(userService.isDuplicate(request.getFirstName(), uniqueSkills, request.getExperience())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)    // HTTP 409 Conflict
                    .body(Map.of("error", "Пользователь с такими данными уже существует"));
        }
        User user = userService.addUser(request.getFirstName(), uniqueSkills, request.getExperience());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
