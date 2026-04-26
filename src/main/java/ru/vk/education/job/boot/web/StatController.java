package ru.vk.education.job.boot.web;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.service.StatService;

import java.util.List;

@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @GetMapping("/exp")
    public ResponseEntity<List<Vacancy>> getMatchesVacancy(@NotNull User user, @NotNull Integer experience) {
        return ResponseEntity.ok(statService.getMatchesVacancy(user, experience));
    }

//    @PostMapping
//    public ResponseEntity<Vacancy>
}
