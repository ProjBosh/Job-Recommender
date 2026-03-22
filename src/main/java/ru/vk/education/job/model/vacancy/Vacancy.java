package ru.vk.education.job.model.vacancy;

import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.HashSet;
import java.util.Set;

public class Vacancy {
    private String jobTitle;
    private String company;
    private Set<String> tags = new HashSet<>();
    private int experience;

    public Vacancy() {}

    public Vacancy(String jobTitle, String company, Set<String> tags, int experience) {
        if (vacancyIsEnteredCorrectly(jobTitle)) {
            this.jobTitle = jobTitle;
            this.company = company;
            this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
            this.experience = experience;
            // Add to storage
            new VacancyRepository().addVacancy(this);
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
        return new HashSet<>(tags);
    }

    public int getExperience() {
        return experience;
    }
}