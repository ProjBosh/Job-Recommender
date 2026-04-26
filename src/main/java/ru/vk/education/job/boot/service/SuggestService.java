package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.VacancyRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SuggestService {
    private final VacancyRepository vacancyRepository;

    public List<Vacancy> getTopSuggestVacancy(User user, int countVacancy) {
        Map<Vacancy, Double> ratingSuggestVacancy = new HashMap<>();

        for(Vacancy vacancy : vacancyRepository.findAll()) {
            int matchCountSkills = (int) vacancyRepository.getTheNumberOfMatchingSkills(vacancy, user);
            double points = matchCountSkills > 0 && user.getExperience() >= vacancy.getExperience()
                            ? matchCountSkills
                            : (double) matchCountSkills / 2;
            if(points > 0)
                ratingSuggestVacancy.put(vacancy, points);
        }

        return ratingSuggestVacancy.entrySet().stream()
                .sorted(Map.Entry.<Vacancy, Double>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(entry -> entry.getKey().getJobName()))
                .limit(countVacancy)
                .map(Map.Entry::getKey)
                .toList();
    }
}
