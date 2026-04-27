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
    private final VacancyService vacancyService;

    /**
     * Получить список вакансий, у которых требуемый опыта больше переданного значения
     *
     * @param minExperience - минимальное количество опыта
     * @return Список вакансий
     */
    public List<Vacancy> getMatchesVacancy(int minExperience) {
        return vacancyRepository.findAll().stream()
                .filter(v -> v.getExperience() >= minExperience)
                .toList();
    }

    /**
     * Получить список пользователей, у которых минимум N подходящих вакансий
     *
     * @param minCountMatch - минимальное количество подходящих вакансий
     * @return Список пользователей
     */
    public List<User> getMatchesUser(int minCountMatch) {
        // Создаем карту рейтинга по числу подходящих вакансий
        Map<User, Integer> ratingVacancies = new HashMap<>();

        // 1. Индексируем вакансии по навыкам (один раз)
        Map<String, List<Vacancy>> vacanciesBySkill = new HashMap<>();
        for (Vacancy vacancy : vacancyRepository.findAll()) {
            for (String tag : vacancy.getTags()) {
                vacanciesBySkill.computeIfAbsent(tag, k -> new ArrayList<>()).add(vacancy);
            }
        }

        // 2. Для каждого пользователя считаем уникальные вакансии
        for (User user : userRepository.findAll()) {
            Set<Vacancy> matchedVacancies = new HashSet<>();

            for (String skill : user.getSkills()) {
                List<Vacancy> candidates = vacanciesBySkill.getOrDefault(skill, List.of());
                matchedVacancies.addAll(candidates);
            }

            int points = matchedVacancies.size();
            if (points > 0) {
                ratingVacancies.put(user, points);
            }
        }

        return ratingVacancies.entrySet().stream()
                .filter(entry -> entry.getValue() >= minCountMatch)
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(User::getFirstName)))
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Получить список Топ-N навыков всех пользователей
     *
     * @param skillCount - количество навыков, которые необходимо получить
     * @return Список Топ-N навыков
     */
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
