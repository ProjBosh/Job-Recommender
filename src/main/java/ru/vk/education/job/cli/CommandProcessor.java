package ru.vk.education.job.cli;

import ru.vk.education.job.model.user.User;
import ru.vk.education.job.model.user.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandProcessor {
    public void split(String inputLine) {
        String[] parts = inputLine.split(" ");
        String command = parts[0];

        switch (command) {
            case "user" -> parseCommandUser(parts);                         // Добавление пользователя
            case "user-list" -> new UserRepository().printList();           // Вывод списка пользователей
            case "job" -> {
                // TODO: Реализовать добавление вакансии
            }
            case "job-list" -> {
                // TODO: Реализовать вывод вакансий
            }
            case "suggest" -> {
                // TODO: Реализовать вывод выводит не больше 2 вакансий
            }
        }
    }
    /*
        Разбиение команды user <name> --skills=... --exp=... на компоненты
        Добавление пользователя в системы
     */
    private void parseCommandUser(String[] parts) {
        String userFirstName = parts[1];

        // Проверяем корректность имени
        if (new User().nameIsEnteredCorrectly(userFirstName)) {
            // Проверяем существование пользователя в системе
            if (!(new UserRepository().find(userFirstName))) {
                try {
                    Set<String> skills = new HashSet<>();
                    int experience = 0;

                    for (int i = 2; i < parts.length; i++) {
                        String param = parts[i];    // например, --skills=java,ml,linux

                        // Убираем "--"
                        String withoutPrefix = param.substring(2);

                        // Разбиваем по "="
                        String[] keyValue = withoutPrefix.split("=");
                        String key = keyValue[0];       // "skills"
                        String value = keyValue[1];     // "java,ml,linux"

                        if (key.equals("skills")) {
                            skills = new HashSet<>(Arrays.asList(value.split(",")));
                        } else if (key.equals("exp")) {
                            experience = Integer.parseInt(value);
                        }
                    }

                    // Добавление нового пользователя
                    User user = new User(userFirstName, skills, experience);
                } catch (Exception e) {
                    System.err.println("Ошибка при обработке имени: " + e.getMessage());
                }
            }
        } else {
            try {
                throw new IllegalArgumentException("Имя пользователя не может быть пустым");
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка валидации: " + e.getMessage());
            }
        }
    }
}
