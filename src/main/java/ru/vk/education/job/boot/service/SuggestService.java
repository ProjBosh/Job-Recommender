package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.UserRepository;
import ru.vk.education.job.boot.repository.VacancyRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SuggestService {
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final VacancyService vacancyService;

    public List<Vacancy> getTopSuggestVacancy(User user, int countVacancy) {
        Map<Vacancy, Double> ratingSuggestVacancy = new HashMap<>();

        for(Vacancy vacancy : vacancyRepository.findAll()) {
            int matchCountSkills = (int) vacancyService.getTheNumberOfMatchingSkills(vacancy, user);
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

    public void findBestVacancies() {
        for(User user : userRepository.findAll()) {
            List<Vacancy> listVacancy = getTopSuggestVacancy(user, 1);
            if(!listVacancy.isEmpty()) {
                Vacancy bestVacancy = listVacancy.get(0);
                System.out.println(user.getFirstName() + ", лучшее предложение - " +
                        bestVacancy.getJobName() + " at " + bestVacancy.getCompany());
            }
        }
    }
}
