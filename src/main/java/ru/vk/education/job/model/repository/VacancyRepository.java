package ru.vk.education.job.model.repository;

import ru.vk.education.job.model.model.Vacancy;

import java.util.*;

public class VacancyRepository {
    private static final List<Vacancy> vacancies = new ArrayList<>();
    private static final Map<String, Vacancy> vacanciesByName = new HashMap<>();

    public VacancyRepository() {}

    /**
     * Сохраняет данные для дальнейшего анализа
     *
     * @param v - Вакансия
     * @param jobName - Название вакансии
     */
    public void save(Vacancy v, String jobName) {
        vacancies.add(v);
        vacancies.sort(Comparator.comparing(Vacancy::getJobName));
        vacanciesByName.put(jobName, v);
    }

    /**
     * Шаблонный вывод данных
     * Метод является закрытым для сохранения шаблона вывода в консоль
     *
     * @param v - Вакансия
     */
    private void printlnVacancy(Vacancy v) {
        System.out.println(v.getJobName() + " at " + v.getCompany());
    }

    /**
     * Вывод всех вакансий из списка
     */
    public void printlnVacancies() {
        vacancies.forEach(this::printlnVacancy);
    }

    /**
     * Вывод всех вакансий, у которых не менее N лет опыта, из списка
     */
    public void printlnListOfMatchingVacancies(int minExperience) {
        vacancies.stream()
                .filter(v -> v.getExperience() >= minExperience)
                .forEach(this::printlnVacancy);
    }

    /**
     * Проверяет наличие пользователя
     *
     * @param jobName - Название вакансии
     * @return Наличие или отсутствие вакансии
     */
    public boolean isPresent(String jobName) {
        return !jobName.trim().isEmpty() && vacanciesByName.containsKey(jobName);
    }

    /**
     * Getters and Setters
     */

    public List<Vacancy> getVacancies(){
        return vacancies;
    }
}
