package ru.vk.education.job.service.storage;

import ru.vk.education.job.model.vacancy.Vacancy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VacancyRepository {
    private static final Set<Vacancy> vacancies = new HashSet<>();
    private static final Map<String, Vacancy> vacancyByName = new HashMap<>();

    public VacancyRepository() {}

    public VacancyRepository(Vacancy vacancy) {
        vacancies.add(vacancy);
        vacancyByName.put(vacancy.getJobTitle(), vacancy);
    }

    public void printList() {
        vacancies.forEach(vacancy -> System.out.println(vacancy.getJobTitle() + " at " +
                    vacancy.getCompany()));
    }

    public boolean find(String vacancy) { return vacancyByName.containsKey(vacancy); }

    public Set<Vacancy> getVacancies() {
        return vacancies;
    }
}
