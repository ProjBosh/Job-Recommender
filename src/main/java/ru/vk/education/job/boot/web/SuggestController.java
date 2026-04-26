package ru.vk.education.job.boot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.service.SuggestService;

import java.util.List;

@RestController
@RequestMapping("/suggest")
@RequiredArgsConstructor
public class SuggestController {
    private final SuggestService suggestService;

    @GetMapping
    public ResponseEntity<List<Vacancy>> getSuggestVacancy(User user) {
        return ResponseEntity.ok(suggestService.getTopSuggestVacancy(user, 2));
    }
}
