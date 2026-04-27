package ru.vk.education.job.boot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;  // ← правильный импорт
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.service.SuggestService;
import ru.vk.education.job.boot.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/suggest")
@RequiredArgsConstructor
public class SuggestController {
    private final SuggestService suggestService;
    private final UserService userService;

    @Value("${suggest.top.limit:2}")
    private int topVacancyLimit;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Vacancy>> getSuggestVacancy(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(suggestService.getTopSuggestVacancy(user, topVacancyLimit));
    }
}