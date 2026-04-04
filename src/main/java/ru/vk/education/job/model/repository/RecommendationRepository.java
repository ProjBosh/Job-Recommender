package ru.vk.education.job.model.repository;

import ru.vk.education.job.model.model.User;
import ru.vk.education.job.service.RecommendationService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RecommendationRepository {
    private static final Map<User, Long> ratingMatches = new HashMap<>();

    public RecommendationRepository() {}

    /**
     * Сохранить пользователя и количество подходящих вакансий
     *
     * @param u - Пользователь
     * @param countMatches - Количество совпадений
     */
    public void save(final User u, final long countMatches) {
        ratingMatches.put(u, countMatches);
    }

    /**
     * Инициализация заполнения рейтинга совпадений вакансий для пользователей
     */
    public void init() {
        new UserRepository().getUsers()
                .forEach(u -> new RecommendationService().suggestVacancy(u.getFirstName(), false));
    }

    /**
     * Получить рейтинг из Топ N пользователей, у которых не менее N подходящих вакансий
     * @param minMatches - Минимальное количество совпадающих параметров
     * @return Поток с отсортировнными и отфильтрованными данными о пользователях
     */
    public Stream<Map.Entry<User, Long>> getTopRating(final int minMatches) {
        return ratingMatches.entrySet().stream()
                .filter(entry -> entry.getValue() >= minMatches)
                .sorted(Map.Entry.<User, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(entry -> entry.getKey().getFirstName()));
    }
}
