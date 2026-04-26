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
        Map<User, Double> ratingVacancies = new HashMap<>();

        // Для каждого пользователя ищем количество подходящих вакансий
        for(User user : userRepository.findAll()) {
            double points = 0;
            // Получаем количество подходящих вакансий для пользователя по навыкам
            for(Vacancy vacancy : vacancyRepository.findAll()) {
                double numberOfMatchingSkills = vacancy.getTags().stream()
                    .filter(user.getSkills()::contains)
                    .count();
                points += numberOfMatchingSkills > 0 ? 1 : 0;
            }
            if (points > 0) {
                // Сохраняем пользователя и количество подходящих ваканий
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
        // TODO: Получить список навыков
        // 1. Обойти каждого пользователя и получить его навыки
        // 2. Собрать рейтинг навыков в Map<String, Long>
        // 3. Получить Топ-N
        // 4. Вернуть List<String>

        return null;
    }
}
