package ru.vk.education.job.cli.model.model;

import ru.vk.education.job.cli.model.repository.RecommendationRepository;
import ru.vk.education.job.cli.model.repository.UserRepository;
import ru.vk.education.job.cli.model.rating.RatingSkills;

import java.util.*;

public class User {
    private String firstName;
    private Set<String> skills = new HashSet<>();
    private int experience;

    public User() {}

    /**
     * Добавляет пользователя с его данными.
     * Дополнительно обновляет статистику по Топ N навыков и инициализирует обновление совпадений у пользователей и вакансий
     *
     * @param firstName - Имя пользователя
     * @param skills - Навыки
     * @param experience - Опыт
     */
    public void add(final String firstName, final Set<String> skills, final int experience) {
        setFirstName(firstName);
        setSkills(skills);
        setExperience(experience);

        new UserRepository().save(this, firstName);

        // Добавление статистики для Топ N навыков
        new RatingSkills(this);

        // Инициализируем заполнение рейтинга совпадений у пользователей по вакансиям
        new RecommendationRepository().init();
    }

    /**
     * Getters and Setters
     */

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public int getExperience() {
        return experience;
    }

    private void setExperience(final int experience) {
        this.experience = experience;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public String getSkillsToString() {
        return String.join(",", getSkills());
    }

    private void setSkills(final Set<String> skills) {
        this.skills = skills;
    }
}
