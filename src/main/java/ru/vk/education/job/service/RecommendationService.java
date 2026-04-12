package ru.vk.education.job.service;

import ru.vk.education.job.BestJobSuggestionService;
import ru.vk.education.job.model.model.User;
import ru.vk.education.job.model.rating.RatingVacancy;
import ru.vk.education.job.model.repository.RecommendationRepository;
import ru.vk.education.job.model.repository.UserRepository;
import ru.vk.education.job.model.repository.VacancyRepository;

import java.util.*;

public class RecommendationService {

    public RecommendationService() {}

    /**
     * Находит пользователя. Получает рейтинг. Выводит рейтинг.
     * Добавляет пользоветеля в хранилище с совпадениями пользователей и вакансий по предлагаемым и требуемым навыкам
     *
     * @param firstName - Имя пользователя
     * @param print - Отметка о необходимости печатать данные в терминал
     */
    public void suggestVacancy(final String firstName, final boolean print) {
        User user = new UserRepository().getUser(firstName);

        // Получаем отфильтрованный рейтинг вакансий (без значений: 0 и null)
        ArrayList<RatingVacancy> ratingVacancies = getRatingVacancies(user);
        // Выводим рейтинг с сортировкой по очкам
        if (print)
            printlnRatingVacancies(ratingVacancies);

        // Добавляем пользователя и количество подходящих в вакансий в хранилище
        new RecommendationRepository().save(user, ratingVacancies.size());
    }

    /**
     * Помогает получить отсортировнный рейтинг вакансий по совпадающим навыкам
     *
     * @param user - Пользователь
     * @return Отсортированный и отфильтрованный рейтинг вакансий
     */
    private ArrayList<RatingVacancy> getRatingVacancies(final User user) {
        ArrayList<RatingVacancy> ratingVacancies = new ArrayList<>();

        new VacancyRepository().getVacancies().stream()
                .map(v -> {
                    double point = v.countCommonSkills(user.getSkills());
                    double points = (point > 0 && user.getExperience() >= v.getExperience() ? point : (point / 2));
                    return points > 0 ? new RatingVacancy(v.getJobName(), points): null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(RatingVacancy::getPoint).reversed())
                .forEach(ratingVacancies::add);

        // Сохраняем рейтинг вакансий для пользователей
        new BestJobSuggestionService().saveRatingVacancyForUser(user, new VacancyRepository().getVacancy(ratingVacancies.get(0).getJobName()));

        return ratingVacancies;
    }

    /**
     * Метод производит вывод рейтинга Топ N вакансий по количеству очков.
     * Чем больше совпадений между навыками пользователя и требуемыми навыками вакансии, тем выше вакансия в рейтинге.
     *
     * @param ratingVacancies - заполненный рейтинг с вакансиями
     */
    private void printlnRatingVacancies(final ArrayList<RatingVacancy> ratingVacancies) {
        long TOP_RESULT = 2;
        ratingVacancies.stream()
                .limit(TOP_RESULT)
                .forEach(rv -> System.out.println(rv.getJobName() + " " + rv.getPoint()));
    }
}

