package ru.vk.education.job.model.vacancy;

import java.util.List;

public class Vacancy {
    private String jobTitle;    // Название вакансии
    private String company;     // Название компании (дальше можно использовать класс Company)
    private List<String> tags;  // Теги (дальше можно использовать класс Tag)
    private int experience;     // Требуемый опыт
}
