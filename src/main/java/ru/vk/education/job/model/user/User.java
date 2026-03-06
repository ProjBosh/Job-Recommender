package ru.vk.education.job.model.user;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String firstName ;                      // Имя пользователя
    private Set<String> skills = new HashSet<>();   // Список навыков (аналог tags в классе Vacancy)
    private int experience;                         // Опыт

    public User() {}

    public User(String firstName, Set<String> skills, int experience) {
        if (new User().nameIsEnteredCorrectly(firstName) && !(new UserRepository().find(firstName))) {
            this.firstName = firstName;
            this.skills = skills;
            this.experience = experience;
            // Добавление в хранилище пользователя
            new UserRepository(this);
        }
    }

    public boolean nameIsEnteredCorrectly(String userFirstName) {
        return (userFirstName != null && !userFirstName.trim().isEmpty() && !userFirstName.contains("--"));
    }

    public String getFirstName() {
        return firstName;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public int getExperience() {
        return experience;
    }
}
