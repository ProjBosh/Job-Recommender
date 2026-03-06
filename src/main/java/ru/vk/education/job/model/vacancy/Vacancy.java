package ru.vk.education.job.model.vacancy;

import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.HashSet;
import java.util.Set;

public class Vacancy {
    private String jobTitle;                        // Название вакансии
    private String company;                         // Название компании (дальше можно использовать класс Company)
    private Set<String> tags = new HashSet<>();     // Список навыков (аналог skills в классе User)
    private int experience;                         // Требуемый опыт

    public Vacancy() {}

    public Vacancy(String jobTitle, String company, Set<String> tags, int experience) {
        // Проверяем корректность названия и существование вакансии
        if (vacancyIsEnteredCorrectly(jobTitle) && !(new VacancyRepository().find(jobTitle))) {
            this.jobTitle = jobTitle;
            this.company = company;
            this.tags = tags;
            this.experience = experience;
            // Добавление в хранилище вакансий
            new VacancyRepository(this);
        }
    }

    public boolean vacancyIsEnteredCorrectly(String vacancy) {
        return (vacancy != null && !vacancy.trim().isEmpty() && !vacancy.contains("--"));
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public Set<String> getTags() {
        return tags;
    }

    public int getExperience() {
        return experience;
    }
}