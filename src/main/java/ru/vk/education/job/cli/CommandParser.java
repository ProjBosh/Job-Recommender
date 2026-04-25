package ru.vk.education.job.cli;

import ru.vk.education.job.cli.model.model.User;
import ru.vk.education.job.cli.model.model.Vacancy;
import ru.vk.education.job.cli.model.rating.RatingSkills;
import ru.vk.education.job.cli.model.repository.UserRepository;
import ru.vk.education.job.cli.model.repository.VacancyRepository;
import ru.vk.education.job.cli.service.FileService;
import ru.vk.education.job.cli.service.RecommendationService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandParser {
    private String command;
    private String[] params;
    private int paramsLength;

    private boolean saveThisCommand;
    int exp;
    String company;
    Set<String> skills = new HashSet<>();

    public CommandParser() {
        params = null;
        command = "";
        saveThisCommand = true;
        paramsLength = 0;
        exp = 0;
        company = "";
    }

    /**
     * Первичная обработка. Распределение команд по методам-обработчикам.
     *
     * @param inputCommand - Входная строка, вводимая в консоль
     * @param saveWithFile - Отметка о сохранение строки в файл или пропуске
     */
    public void execute(final String inputCommand, boolean saveWithFile) {
        FileService fileService = new FileService();

        command = inputCommand.trim();
        params = inputCommand.split(" ");
        paramsLength = params.length;

        String baseCommand = params[0];
        String name = paramsLength > 1 ? params[1] : "";

        switch(baseCommand) {
            case "user" -> {
                if (nameIsEmpty(name))
                    return;
                parseUserCommand(name);
            }
            case "user-list" -> new UserRepository().printlnUsers();
            case "job" -> {
                if (nameIsEmpty(name))
                    return;
                parseVacancyCommand(name);
            }
            case "job-list" -> new VacancyRepository().printlnVacancies();
            case "suggest" -> {
                if (nameIsEmpty(name))
                    return;
                new RecommendationService().suggestVacancy(name, true);
            }
            case "history" -> fileService.printlnCommandWithFile();
            case "stat" -> {
                if (nameIsEmpty(name))
                    return;
                parseStatCommand(name);
            }
            default -> saveWithFile = false;
        }

        if(saveThisCommand && saveWithFile)
            fileService.saveCommand(command);
    }

    /**
     * Проверка нескольких параметров.
     *
     * @return Отметка об одном или о нескольких параметрах
     */
    private boolean atLeastOneParameter() {
        return paramsLength > 1;
    }

    /**
     * Проверяем заполнена ли строка или нет.
     *
     * @param name - Имя второстепенной команды
     * @return Возвращает True, если имя заполнено.
     */
    private boolean nameIsEmpty(final String name) {
        return name.trim().isEmpty();
    }

    /**
     * Разделяет строку ститистики на компоненты и вносит данные по компонентам
     *
     * @param name - Имя второстепенной команды
     */
    public void parseStatCommand(final String name) {
        if (atLeastOneParameter()) {
            String cmdFull = name.trim();
            int value = Integer.parseInt(params[2].trim());
            if (!cmdFull.startsWith("--") || value == 0)
                return;

            switch(cmdFull.substring(2)) {
                case "exp" -> new VacancyRepository().printlnListOfMatchingVacancies(value);
                case "match" -> new UserRepository().printlnListOfMatchingUsers(value);
                case "top-skills" -> new RatingSkills().printlnTopSkills(value);
            }
        }
    }

    /**
     * Обрабатывает данные пользователя
     *
     * @param firstName - Имя второстепенной команды
     */
    public void parseUserCommand(final String firstName) {
        if (atLeastOneParameter()) {
            if (new UserRepository().isPresent(firstName))
                return;

            try {
                parseArgs();
                new User().add(firstName, skills, exp);
            } catch (Exception e) {
                // Игнорируем
            }
        } else
            saveThisCommand = false;
    }

    /**
     * Обрабатывает данные вакансии
     *
     * @param jobName - Имя второстепенной команды
     */
    public void parseVacancyCommand(final String jobName) {
        if (atLeastOneParameter()) {
            if (new VacancyRepository().isPresent(jobName))
                return;

            try {
                parseArgs();
                new Vacancy().add(jobName, company, skills, exp);
            } catch (Exception e) {
                // Игнорируем
            }
        } else
            saveThisCommand = false;
    }

    /**
     * Разбирает строку параметров на компоненны
     */
    private void parseArgs() {
        Map<String, String> argsMap = Arrays.stream(params)
                .skip(2)                                                // Пропускаем 2 элементов
                .map(String::trim)                                         // Убираем пробелы
                .filter(p -> p.startsWith("--") && p.contains("=")) // Оставляем только валидные аргументы
                .map(p -> p.substring(2).split("="))
                // Если ключ дублируется, берем последний
                .collect(Collectors.toMap(
                        p -> p[0],
                        p -> p[1],
                        (existing, replacement) -> replacement)
                );

                company = argsMap.getOrDefault("company", company);
                exp = Integer.parseInt(argsMap.getOrDefault("exp", String.valueOf(exp)));

                String skillsRaw = argsMap.getOrDefault("skills", argsMap.get("tags"));
                if (skillsRaw != null)
                    skills = new HashSet<>(Arrays.asList(skillsRaw.split(",")));
    }
}
