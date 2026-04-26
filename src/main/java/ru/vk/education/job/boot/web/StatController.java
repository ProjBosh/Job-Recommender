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

    /**
     * Поиск и получение вакансий с опытом не меньше переданного числа
     * @param experience - минимальное количество лет опыта
     * @return Список подходящих вакансий
     */
    @GetMapping("/exp")
    public ResponseEntity<List<Vacancy>> getMatchesVacancy(int experience) {
        if (experience < 0) {
            experience = 0;
        }
        return ResponseEntity.ok(statService.getMatchesVacancy(experience));
    }

    /**
     * Поиск и получение пользователей с количеством подходящих вакансий не меньше переданного числа
     * @param matchCount - минимальное количество подходящих вакансий
     * @return Список подходящих пользователей
     */
    @GetMapping("/match")
    public ResponseEntity<List<User>> getMatchesUser(int matchCount) {
        if (matchCount < 0) {
            matchCount = 0;
        }
        return ResponseEntity.ok(statService.getMatchesUser(matchCount));
    }

    /**
     * Поиск и получение Топ-N указанных пользователями навыков
     * @param skillCount - количестов получаемых навыков
     * @return Список Топ-N навыков
     */
    @GetMapping("/top-skills")
    public ResponseEntity<List<String>> getTopSkills(int skillCount) {
        if (skillCount < 0) {
            skillCount = 0;
        }
        return ResponseEntity.ok(statService.getTopSkills(skillCount));
    }
}
