package ru.vk.education.job.service.recommendation;

import ru.vk.education.job.model.user.User;
import ru.vk.education.job.model.vacancy.Vacancy;
import ru.vk.education.job.service.storage.UserRepository;
import ru.vk.education.job.service.storage.VacancyRepository;

import java.util.*;

public class RecommendationService {
    public RecommendationService() {}

    public void findVacancy(String firstName) {
        User user = new UserRepository().getUser(firstName);

        // Check if user exists
        if (user == null) {
            System.out.println("No matches found");
            return;
        }

        List<Vacancy> vacancies = new VacancyRepository().getVacanciesList();
        
        // Create map with ratings
        Map<Vacancy, Double> vacanciesRating = new HashMap<>();

        for (Vacancy vacancy : vacancies) {
            double matchCount = calculateRating(vacancy, user.getSkills(), user.getExperience());
            if (matchCount > 0.0) {
                vacanciesRating.put(vacancy, matchCount);
            }
        }

        if (vacanciesRating.isEmpty()) {
            System.out.println("No matches found");
            return;
        }

        // Sort by rating descending
        List<Vacancy> result = new ArrayList<>(vacanciesRating.keySet());
        result.sort((v1, v2) -> {
            Double score1 = vacanciesRating.get(v1);
            Double score2 = vacanciesRating.get(v2);
            return Double.compare(score2, score1);
        });

        // Output top 2 vacancies
        int count = Math.min(2, result.size());
        for (int i = 0; i < count; i++) {
            Vacancy vacancy = result.get(i);
            System.out.println(vacancy.getJobTitle() + " at " + vacancy.getCompany());
        }
    }

    private double calculateRating(Vacancy vacancy, Set<String> skills, int experience) {
        // Count matching skills
        Set<String> matchedSkills = new HashSet<>(skills);
        matchedSkills.retainAll(vacancy.getTags());
        int matchingCount = matchedSkills.size();

        // If experience is insufficient, divide the count by 2
        if (experience < vacancy.getExperience()) {
            return matchingCount / 2.0;
        }
        return matchingCount;
    }
}