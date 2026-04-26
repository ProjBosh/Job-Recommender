package ru.vk.education.job.boot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.service.VacancyService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {
    private final VacancyService vacancyService;

    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllVacancies() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }

    @PostMapping
    public ResponseEntity<?> addVacancy(@RequestBody Vacancy request) {
        Set<String> uniqueTags = new HashSet<>(request.getTags());

        if(vacancyService.isDuplicate(request.getJobName(), request.getCompany(), uniqueTags, request.getExperience())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)    // 409 Conflict
                    .body(Map.of("error", "Вакансия с такими данными уже существует"));
        }
        Vacancy vacancy = vacancyService.addVacancy(request.getJobName(), request.getCompany(), uniqueTags, request.getExperience());
        return ResponseEntity.status(HttpStatus.CREATED).body(vacancy);
    }
}
