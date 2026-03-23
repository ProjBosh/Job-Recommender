package ru.vk.education.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandParser {
    private String command;
    private String[] params;
    private int paramsLength;

    private boolean saveThisCommand;
    int exp;
    String company;
    Set<String> skills = new HashSet<>();

    private final User user = new User();
    private final Vacancy vacancy = new Vacancy();
    private final FileService fileService = new FileService();

    public CommandParser() {
        params = null;
        command = "";
        saveThisCommand = true;
        paramsLength = 0;
        exp = 0;
        company = "";
    }

    public void execute(String inputCommand, boolean saveWithFile) {
        command = inputCommand.trim();
        params = inputCommand.split(" ");
        paramsLength = params.length;

        String baseCommand = params[0];

        switch(baseCommand) {
            case "user" -> parseUserCommand();
            case "user-list" -> user.println();
            case "job" -> parseVacancyCommand();
            case "job-list" -> vacancy.println();
            case "suggest" -> new RecommendationService().suggestVacancy(params[1]);
            case "history" -> fileService.printlnCommandWithFile();
        }

        if(saveThisCommand && saveWithFile) {
            fileService.saveCommand(command);
        }
    }

    public void parseUserCommand() {
        if (paramsLength > 1) {
            String firstName = params[1];

            if (user.isPresent(firstName))
                return;

            try {
                parseArgs();
                user.add(firstName, skills, exp);
            } catch (Exception e) {
                // Игнорируем
            }
        } else {
            saveThisCommand = false;
        }
    }

    public void parseVacancyCommand() {
        if (paramsLength > 1) {
            String jobName = params[1];

            if (vacancy.isPresent(jobName))
                return;

            try {
                parseArgs();
                vacancy.add(jobName, company, skills, exp);
            } catch (Exception e) {
                // Игнорируем
            }
        } else {
            saveThisCommand = false;
        }
    }

    private void parseArgs() {
        int startIndex = 2;

        for (int i = startIndex; i < paramsLength; i++) {
            String p = params[i].trim();

            if (p.startsWith("--")) {
                String withoutPrefix = p.substring(2);
                int pos = withoutPrefix.indexOf("=");

                if (pos != -1) {
                    String value = withoutPrefix.substring(pos + 1);

                    // Проверяем по ключу (key)
                    switch (withoutPrefix.substring(0, pos)) {
                        case "company" -> company = value;
                        case "skills", "tags" -> skills = new HashSet<>(Arrays.asList(value.split(",")));
                        case "exp" -> exp = Integer.parseInt(value);
                    }
                }
            }
        }
    }
}
