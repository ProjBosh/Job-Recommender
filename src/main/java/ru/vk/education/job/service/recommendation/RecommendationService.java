package ru.vk.education.job.service.recommendation;

import ru.vk.education.job.model.user.User;
import ru.vk.education.job.model.vacancy.Vacancy;
import ru.vk.education.job.service.storage.UserRepository;
import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class RecommendationService {

    public RecommendationService() {}

    public void findVacancy(String firstName) {
        User username = new UserRepository().getUser(firstName);
        Set<String> skills = username.getSkills();
        int experience = username.getExperience();
        Set<Vacancy> vacancies = new VacancyRepository().getVacancies();

        // Используем LinkedHashMap для сохранения порядка сортировки
        Map<String, Double> ratings = new LinkedHashMap<>();

        // Обход каждой вакансии
        for (Vacancy vacancy : vacancies) {
            double countMatchingSkills = getCountMatchingSkills(vacancy, skills, experience);

            ratings.put(vacancy.getJobTitle(), countMatchingSkills);
        }

        // Сортировка по убыванию (более логично для рейтинга)
        List<Map.Entry<String, Double>> sortedList = ratings.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .toList();

        // Вывод результатов
        sortedList.stream()
                .limit(2)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    private static double getCountMatchingSkills(Vacancy vacancy, Set<String> skills, int experience) {
        double countMatchingSkills = 0.0;
        Set<String> vacancyTags = vacancy.getTags(); // Сохраняем в локальную переменную

        // Проверяем пересечение множеств для оптимизации
        Set<String> matchedSkills = new HashSet<>(skills);
        matchedSkills.retainAll(vacancyTags); // Оставляем только совпадающие навыки

        // Считаем совпадения
        for (String matchedSkill : matchedSkills) {
            countMatchingSkills += (experience >= vacancy.getExperience() ? 1 : 0.5);
        }

        return countMatchingSkills;
    }
}
