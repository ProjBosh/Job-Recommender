package ru.vk.education.job.service.storage;

import ru.vk.education.job.model.vacancy.Vacancy;

import java.util.*;

public class VacancyRepository {
    private static final List<Vacancy> vacancies = new ArrayList<>();
    private static final Map<String, Vacancy> vacancyByName = new HashMap<>();

    public VacancyRepository() {}

    public boolean addVacancy(Vacancy vacancy) {
        if (vacancy == null || vacancy.getJobTitle() == null) {
            return false;
        }
        
        // Check if vacancy already exists
        if (vacancyByName.containsKey(vacancy.getJobTitle())) {
            return false;
        }
        
        vacancies.add(vacancy);
        vacancyByName.put(vacancy.getJobTitle(), vacancy);
        return true;
    }

    public void printList() {
        for (Vacancy vacancy : vacancies) {
            System.out.println(vacancy.getJobTitle() + " at " + vacancy.getCompany());
        }
    }

    public boolean find(String vacancyTitle) { 
        return vacancyByName.containsKey(vacancyTitle); 
    }

    public Set<Vacancy> getVacancies() {
        return new HashSet<>(vacancies);
    }
    
    public List<Vacancy> getVacanciesList() {
        return new ArrayList<>(vacancies);
    }
}