package ru.vk.education.job.model.user;

import java.util.List;

public class User {
    private String firstName ;      // Имя пользователя
    private List<String> skills;    // Список навыков
    private int experience;         // Опыт

    public User(String firstName, List<String> skills, int experience) {
        this.firstName = firstName;
        this.skills = skills;
        this.experience = experience;
    }
}
