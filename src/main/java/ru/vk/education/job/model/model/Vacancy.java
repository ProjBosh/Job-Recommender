package ru.vk.education.job.model.model;

import ru.vk.education.job.model.repository.RecommendationRepository;
import ru.vk.education.job.model.repository.VacancyRepository;

import java.util.*;

public class Vacancy {
    private String company;
    private String jobName;
    private Set<String> tags = new HashSet<>();
    private int experience;

    public Vacancy() {}

    /**
     * Добавляет вакансию с ее параметрами.
     * Дополнительно инициализирует обновление совпадений у пользователей и вакансий
     *
     * @param jobName - Название вакансии
     * @param company - Компания
     * @param tags - Навыки
     * @param experience - Опыт
     */
    public void add(final String jobName, final String company, final Set<String> tags, final int experience) {
        setJobName(jobName);
        setCompany(company);
        setTags(tags);
        setExperience(experience);

        new VacancyRepository().save(this, jobName);

        // Инициализируем заполнение рейтинга совпадений у пользователей по вакансиям
        new RecommendationRepository().init();
    }

    /**
     * Возвращает количество совпадающих навыков у пользователя с требуемыми навыками в вакансии
     *
     * @param skills - навыки пользователя
     * @return Количество совпадений
     */
    public long countCommonSkills(final Set<String> skills){
        return tags.stream()
                .filter(skills::contains)
                .count();
    }

    /**
     * Getters and Setters
     */

    public String getCompany() {
        return company;
    }

    private void setCompany(final String company) {
        this.company = company;
    }

    public String getJobName() {
        return jobName;
    }

    private void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    private void setTags(final Set<String> tags) {
        this.tags = tags;
    }

    public int getExperience() {
        return experience;
    }

    private void setExperience(final int experience) {
        this.experience = experience;
    }
}
