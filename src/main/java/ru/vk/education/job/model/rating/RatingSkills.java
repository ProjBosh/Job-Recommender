package ru.vk.education.job.model.rating;

import ru.vk.education.job.model.model.User;

import java.util.*;

public class RatingSkills {
    private static final Map<String, Long> ratingSkills = new HashMap<>();

    public RatingSkills() {}

    public RatingSkills(final User u) {
        updateStatistic(u.getSkills());
    }

    /**
     * Обновляет статистику по Топ N навыков
     *
     * @param skills - навыки нового или существующего пользователя
     */
    private void updateStatistic(final Set<String> skills) {
        skills.forEach(key -> ratingSkills.merge(key, 1L, Long::sum));
    }

    public void printlnTopSkills(final int countRows) {
        ratingSkills.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(countRows)
                .forEach(entry -> System.out.println(entry.getKey()));
    }
}
