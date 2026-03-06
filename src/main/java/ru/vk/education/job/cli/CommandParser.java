package ru.vk.education.job.cli;

import ru.vk.education.job.model.user.User;
import ru.vk.education.job.model.vacancy.Vacancy;
import ru.vk.education.job.service.storage.UserRepository;
import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandParser {

    public CommandParser() {}

    /*
        Разбиение команды user <name> --skills=... --exp=... на компоненты
        Добавление пользователя
     */
    public void parseCommandUser(String[] parts) {
        String userFirstName = parts[1];

        // Проверяем корректность имени
        if (new User().nameIsEnteredCorrectly(userFirstName)) {
            // Проверяем существование пользователя
            if (!(new UserRepository().find(userFirstName))) {
                try {
                    Set<String> skills = new HashSet<>();
                    int experience = 0;

                    for (int i = 2; i < parts.length; i++) {
                        String param = parts[i];    // например, --skills=java,ml,linux --exp=2

                        // Убираем "--"
                        String withoutPrefix = param.substring(2);

                        // Разбиваем по "="
                        String[] keyValue = withoutPrefix.split("=");
                        String key = keyValue[0];       // "skills / exp"
                        String value = keyValue[1];     // "java,ml,linux / 2"

                        if (key.equals("skills")) {
                            skills = new HashSet<>(Arrays.asList(value.split(",")));
                        } else if (key.equals("exp")) {
                            experience = Integer.parseInt(value);
                        }
                    }

                    // Добавление нового пользователя
                    new User(userFirstName, skills, experience);
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

    /*
        Разбиение команды job <title> --company=... --skills=... --exp=... на компоненты
        Добавление вакансии
     */
    public void parseCommandVacancy(String[] parts) {
        String jobTitle = parts[1];

        // Проверяем корректность вакансии
        if (new Vacancy().vacancyIsEnteredCorrectly(jobTitle)) {
            // Проверяем существование вакансии
            if (!new VacancyRepository().find(jobTitle)) {
                try {
                    String company = "";
                    Set<String> tags = new HashSet<>();
                    int experience = 0;

                    for (int i = 2; i < parts.length; i++) {
                        String param = parts[i];    // например, --company=VK --tags=java,ml,linux --exp=1

                        // Убираем "--"
                        String withoutPrefix = param.substring(2);

                        // Разбиваем по "="
                        String[] keyValue = withoutPrefix.split("=");
                        String key = keyValue[0];       // "company / tags / exp"
                        String value = keyValue[1];     // "VK / java,ml,linux / 1"

                        switch (key) {
                            case "company" -> company = value;
                            case "tags" -> tags = new HashSet<>(Arrays.asList(value.split(",")));
                            case "exp" -> experience = Integer.parseInt(value);
                        }
                    }

                    // Добавление новой вакансии
                    new Vacancy(jobTitle, company, tags, experience);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка при обработке названия вакансии: " + e.getMessage());
                }
            }
        } else {
            try {
                throw new IllegalArgumentException("Название вакансии не может быть пустым");
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка валидации: " + e.getMessage());
            }
        }
    }
}
