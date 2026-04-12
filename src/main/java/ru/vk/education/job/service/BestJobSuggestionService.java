package ru.vk.education.job.service;

import ru.vk.education.job.model.model.User;
import ru.vk.education.job.model.model.Vacancy;
import ru.vk.education.job.model.repository.VacancyRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BestJobSuggestionService implements Runnable {
    private static Map<User, Vacancy> map = new HashMap<>();

    public BestJobSuggestionService() {}

    /**
     * Запуск ассинхронного метода.
     * Сначала сортирует всю карту по алфавиту, затем выводит лучшие предложения
     */
    @Override
    public void run() {
        // Сортируем по алфавиту нашу карту
        map = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(User::getFirstName)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        // Выводим лучшие вакансии для каждого пользователя
        printlnBestJob();
    }

    /**
     * Сохраняет в карту пользователя и его наилучшую ваакансию на данный момент
     *
     * @param user - Пользователь
     * @param vacancy - Вакансия
     */
    public void saveRatingVacancyForUser(final User user, final Vacancy vacancy){
        if (map.containsKey(user))
            map.replace(user, vacancy);
        else
            map.put(user, vacancy);
    }

    /**
     * Обходит всех пользователей и выводит лучшую вакансию для пользователя на данный момент
     */
    public void printlnBestJob() {
        if (!map.isEmpty())
            map.forEach((u, v) -> System.out.println(u.getFirstName() + ", лучшее предложение — " +
                    new VacancyRepository().getAJobWithdrawalTemplate(v)));
    }
}
