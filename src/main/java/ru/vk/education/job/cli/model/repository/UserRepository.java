package ru.vk.education.job.cli.model.repository;

import ru.vk.education.job.cli.model.model.User;

import java.util.*;

public class UserRepository {
    private static final List<User> users = new ArrayList<>();
    private static final Map<String, User> usersByName = new HashMap<>();

    public UserRepository() {}

    /**
     * Сохраняет данные для дальнейшего анализа
     *
     * @param u - Пользователь
     * @param firstName - Имя пользователя
     */
    public void save(User u, String firstName) {
        users.add(u);
        users.sort(Comparator.comparing(User::getFirstName));
        usersByName.put(firstName, u);
    }

    /**
     * Шаблонный вывод данных
     * Метод является закрытым для сохранения шаблона вывода в консоль
     *
     * @param u - Пользователь
     */
    private void printlnUser(User u) {
        System.out.println(u.getFirstName() + " " + u.getSkillsToString() + " " + u.getExperience());
    }

    /**
     * Вывод всех пользователей из списка
     */
    public void printlnUsers() {
        users.forEach(this::printlnUser);
    }

    /**
     * Вывод всех пользователей, у которых не менее N совпадений, из списка
     */
    public void printlnListOfMatchingUsers(int minMatches) {
        new RecommendationRepository().getTopRating(minMatches)
                .forEach(e -> printlnUser(e.getKey()));
    }

    /**
     * Проверяет наличие пользователя
     *
     * @param firstName - Имя пользователя
     * @return Наличие или отсутствие пользователя
     */
    public boolean isPresent(String firstName) {
        return !firstName.trim().isEmpty() && usersByName.containsKey(firstName);
    }

    /**
     * Getters and Setters
     */

    public User getUser(String firstName) {
        return usersByName.get(firstName);
    }

    public List<User> getUsers() {
        return users;
    }
}
