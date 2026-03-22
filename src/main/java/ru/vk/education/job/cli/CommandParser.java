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

    public void parseCommandUser(String[] parts) {
        if (parts.length < 2) {
            return;
        }
        
        String userFirstName = parts[1];

        // Check if user already exists
        if (new UserRepository().find(userFirstName)) {
            return; // User already exists, don't add again
        }

        // Check if name is valid
        if (!new User().nameIsEnteredCorrectly(userFirstName)) {
            return;
        }

        try {
            Set<String> skills = new HashSet<>();
            int experience = 0;

            for (int i = 2; i < parts.length; i++) {
                String param = parts[i];
                
                if (!param.startsWith("--")) {
                    continue;
                }

                // Remove "--"
                String withoutPrefix = param.substring(2);
                
                // Split by "="
                String[] keyValue = withoutPrefix.split("=", 2);
                if (keyValue.length != 2) {
                    continue;
                }
                
                String key = keyValue[0];
                String value = keyValue[1];

                if (key.equals("skills")) {
                    String[] skillArray = value.split(",");
                    skills = new HashSet<>(Arrays.asList(skillArray));
                } else if (key.equals("exp")) {
                    experience = Integer.parseInt(value);
                }
            }

            // Create new user (this will automatically add to repository)
            new User(userFirstName, skills, experience);
        } catch (Exception e) {
            // Ignore parsing errors
        }
    }

    public void parseCommandVacancy(String[] parts) {
        if (parts.length < 2) {
            return;
        }
        
        String jobTitle = parts[1];

        // Check if vacancy already exists
        if (new VacancyRepository().find(jobTitle)) {
            return; // Vacancy already exists, don't add again
        }

        // Check if title is valid
        if (!new Vacancy().vacancyIsEnteredCorrectly(jobTitle)) {
            return;
        }

        try {
            String company = "";
            Set<String> tags = new HashSet<>();
            int experience = 0;

            for (int i = 2; i < parts.length; i++) {
                String param = parts[i];
                
                if (!param.startsWith("--")) {
                    continue;
                }

                // Remove "--"
                String withoutPrefix = param.substring(2);
                
                // Split by "="
                String[] keyValue = withoutPrefix.split("=", 2);
                if (keyValue.length != 2) {
                    continue;
                }
                
                String key = keyValue[0];
                String value = keyValue[1];

                switch (key) {
                    case "company" -> company = value;
                    case "tags" -> {
                        String[] tagArray = value.split(",");
                        tags = new HashSet<>(Arrays.asList(tagArray));
                    }
                    case "exp" -> experience = Integer.parseInt(value);
                }
            }

            // Create new vacancy (this will automatically add to repository)
            new Vacancy(jobTitle, company, tags, experience);
        } catch (Exception e) {
            // Ignore parsing errors
        }
    }
}