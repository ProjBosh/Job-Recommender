package ru.vk.education.job.service;

import ru.vk.education.job.model.model.User;
import ru.vk.education.job.model.model.Vacancy;
import ru.vk.education.job.model.repository.UserRepository;
import ru.vk.education.job.model.repository.VacancyRepository;

import java.util.*;

public class BestJobSuggestionService implements Runnable {
    public BestJobSuggestionService() {}

    /**
     * Запуск ассинхронного метода.
     * Сначала сортирует всю карту по алфавиту, затем выводит лучшие предложения
     */
    @Override
    public void run() {
        // Выводим лучшее предложение для каждого пользователя
        printlnBestJob();
    }

    /**
     * Обходит всех пользователей и выводит лучшую вакансию для пользователя на данный момент
     */
    public void printlnBestJob() {
        List<Vacancy> vacancies = new VacancyRepository().getVacancies();

        // Выводим лучшие вакансии для каждого пользователя
        new UserRepository().getUsers().stream()
                .map(u -> {
                    Vacancy bestVacancy = findBestVacancyForUser(u, vacancies);
                    return bestVacancy != null
                            ? u.getFirstName() + ", лучшее предложение — " + new VacancyRepository().getAJobWithdrawalTemplate(bestVacancy)
                            : null;
                })
                .filter(Objects::nonNull)
                .forEach(System.out::println);
    }

    /**
     * Ищет лучшую вакансию для пользователя
     *
     * @param u - Пользователь
     * @param vacancies - Список вакансий
     * @return Лучшую вакансию для пользователя
     */
    public Vacancy findBestVacancyForUser(User u, List<Vacancy> vacancies) {
        Vacancy bestVacancy = null;
        double maxPoints = 0;

        for (Vacancy v : vacancies) {
            double point = v.countCommonSkills(u.getSkills());
            if (point > 0) {
                double points = u.getExperience() >= v.getExperience() ? point : (point / 2);
                if (maxPoints < points) {
                    bestVacancy = v;
                    maxPoints = points;
                }
            }
        }

        return bestVacancy;
    }
}
