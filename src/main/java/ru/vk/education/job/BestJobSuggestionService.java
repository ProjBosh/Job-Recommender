package ru.vk.education.job;

import ru.vk.education.job.model.model.User;
import ru.vk.education.job.model.model.Vacancy;
import ru.vk.education.job.model.repository.VacancyRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

// На более ранних этапах ты уже создал(а) скелет работающего приложения, создал(а) сущности, а также простой CLI (Command Line Interface) интерфейс для работы с ними.
// В рамках текущей домашки №4 добавим немного асинхронности.
// Нужно реализовать фоновый процесс (код, работающий в отдельном потоке), который регулярно (например, раз в минуту) будет искать лучшее предложение по работе для каждого
// юзера в системе.
// Для простоты, пусть эти предложения просто показываются в консоль, например, в виде:
// ```
// Bob, лучшее предложение — ML Engineer в Google
// Alice, лучшее предложение — Java Developer at VK
// ```
// Чтобы запускать код в отдельном потоке, да еще и периодически, нужно в `Main` классе создать нужный `ExecutorService`. Это сервис, который будет запускать ваш код в
// отдельном потоке.
// Далее у него нужно вызвать нужный `scheduleXXX()` метод. Их всего 4 варианта. Разобраться, какой именно нужен.
// Код, который будет искать лучшее предложение для каждого юзера, реализовать в отдельном классе. Экземпляр этого класса будет передаваться в метод `scheduleXXX()`.
// Также стоит разобраться с корректным плавным завершением работы `ExecutorService`

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
