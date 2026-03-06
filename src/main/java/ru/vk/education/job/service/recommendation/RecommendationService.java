package ru.vk.education.job.service.recommendation;

import ru.vk.education.job.model.user.User;
import ru.vk.education.job.model.vacancy.Vacancy;
import ru.vk.education.job.service.storage.UserRepository;
import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.*;

public class RecommendationService {

    private int RECOMMENDATION_LIMIT = 2;

    public RecommendationService() {}

    public void findVacancy(String firstName) {
        User username = new UserRepository().getUser(firstName);

        // Используем LinkedHashMap для сохранения порядка сортировки
        Map<Vacancy, Double> ratings = new LinkedHashMap<>();

        // Обход каждой вакансии
        for (Vacancy vacancy : new VacancyRepository().getVacancies()) {
            double countMatchingSkills = getCountMatchingSkills(vacancy, username.getSkills(), username.getExperience());

            ratings.put(vacancy, countMatchingSkills);
        }

        // Сортировка по убыванию (более логично для рейтинга)
        List<Map.Entry<Vacancy, Double>> sortedList = ratings.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        // Вывод результатов
        sortedList.stream()
                .limit(RECOMMENDATION_LIMIT)
                .forEach(entry -> System.out.println(entry.getKey().getJobTitle() + " at " + entry.getKey().getCompany()));
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
