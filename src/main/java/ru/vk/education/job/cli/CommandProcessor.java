package ru.vk.education.job.cli;

import ru.vk.education.job.model.user.User;
import ru.vk.education.job.service.storage.UserRepository;
import ru.vk.education.job.model.vacancy.Vacancy;
import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandProcessor {
    public void split(String inputLine) {
        String[] parts = inputLine.split(" ");
        String command = parts[0];
        CommandParser commandParser = new CommandParser();

        switch (command) {
            case "user" -> commandParser.parseCommandUser(parts);           // Добавление пользователя
            case "user-list" -> new UserRepository().printList();           // Вывод списка пользователей
            case "job" -> commandParser.parseCommandVacancy(parts);         // Добавление вакансии
            case "job-list" -> new VacancyRepository().printList();         // Вывод списка вакансий
            case "suggest" -> {
                // TODO: Реализовать вывод выводит не больше 2 вакансий
            }
        }
    }
}
