package ru.vk.education.job.cli;

import ru.vk.education.job.service.history.FileServices;
import ru.vk.education.job.service.recommendation.RecommendationService;
import ru.vk.education.job.service.storage.UserRepository;
import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.List;

public class CommandProcessor {
    
    public void split(String inputLine) {
        if (inputLine == null || inputLine.trim().isEmpty()) {
            return;
        }
        
        String[] parts = inputLine.split(" ");
        if (parts.length == 0) {
            return;
        }
        
        String command = parts[0];
        CommandParser commandParser = new CommandParser();
        FileServices fileServices = new FileServices();
        boolean commandVerified = false;

        switch (command) {
            case "user" -> {
                if (parts.length >= 2) {
                    commandParser.parseCommandUser(parts);
                    commandVerified = true;
                }
            }
            case "user-list" -> {
                new UserRepository().printList();
                commandVerified = true;
            }
            case "job" -> {
                if (parts.length >= 2) {
                    commandParser.parseCommandVacancy(parts);
                    commandVerified = true;
                }
            }
            case "job-list" -> {
                new VacancyRepository().printList();
                commandVerified = true;
            }
            case "suggest" -> {
                if (parts.length >= 2) {
                    new RecommendationService().findVacancy(parts[1]);
                    commandVerified = true;
                }
            }
            case "history" -> {
                List<String> commands = fileServices.getListCommand();
                fileServices.printListCommand(commands);
                commandVerified = true;
            }
        }

        if (commandVerified) {
            // Сохраняем команду в файл
            fileServices.saveCommand(inputLine);
        }
    }
}