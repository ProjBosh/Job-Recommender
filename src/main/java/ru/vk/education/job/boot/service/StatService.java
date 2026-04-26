package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.UserRepository;
import ru.vk.education.job.boot.repository.VacancyRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatService {
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;

    public List<Vacancy> getMatchesVacancy(int minExperience) {
        return vacancyRepository.findAll().stream()
                .filter(v -> v.getExperience() >= minExperience)
                .toList();
    }

    public List<User> getMatchesUser(int minCountMatch) {
        // Создаем карту рейтинга по числу подходящих вакансий
        Map<User, Integer> ratingVacancies = new HashMap<>();

        // Для каждого пользователя ищем количество подходящих вакансий
        for(User user : userRepository.findAll()) {
            // Получаем количество подходящих вакансий для пользователя по навыкам
            int points = (int) vacancyRepository.findAll().stream()
                    .filter(v -> vacancyRepository.getTheNumberOfMatchingSkills(v, user) > 0)
                    .count();
            // Сохраняем пользователя и количество подходящих ваканий
            if (points > 0) {
                ratingVacancies.put(user, points);
            }
        }

        return ratingVacancies.entrySet().stream()
                .filter(entry -> entry.getValue() >= minCountMatch)
                .sorted(Comparator.comparing(entry -> entry.getKey().getFirstName()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<String> getTopSkills(int skillCount) {
        // Создаем карту для рейтинга
        Map<String, Integer> ratingSkills = new HashMap<>();

        // Получаем рейтинг популярности по навыкам пользователей
        for(User user : userRepository.findAll()) {
            for(String skill : user.getSkills()) {
                ratingSkills.put(skill, ratingSkills.containsKey(skill) ? ratingSkills.get(skill) + 1 : 1);
            }
        }

        // Получаем Топ-N и возваращаем список навыков
        return ratingSkills.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(skillCount)
                .map(Map.Entry::getKey)
                .toList();
    }
}
