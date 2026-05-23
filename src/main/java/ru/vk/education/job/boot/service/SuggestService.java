package ru.vk.education.job.boot.service;

import com.github.dockerjava.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.UserRepository;
import ru.vk.education.job.boot.repository.JobRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SuggestService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final VacancyService vacancyService;

    private static final int DEFAULT_VACANCY_COUNT = 2;

    public List<Vacancy> suggest(String userName) {
        User user = userRepository.findByFirstName(userName)
                .orElseThrow(() -> new NotFoundException("User not found: " + userName));
        return getTopSuggestVacancy(user, DEFAULT_VACANCY_COUNT);
    }

    public List<Vacancy> suggest(String userName, int countVacancy) {
        User user = userRepository.findByFirstName(userName)
                .orElseThrow(() -> new NotFoundException("User not found: " + userName));
        return getTopSuggestVacancy(user, countVacancy);
    }

    /**
     * Получить Топ-N вакансий, подходящих пользователю
     *
     * @param user - пользователь
     * @param countVacancy - количество вакансий
     * @return Список вакансий
     */
    public List<Vacancy> getTopSuggestVacancy(User user, int countVacancy) {
        Map<Vacancy, Double> ratingSuggestVacancy = new HashMap<>();

        for(Vacancy vacancy : jobRepository.findAll()) {
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

    /**
     * Подобрать лучшую вакансию для пользователей
     */
    public void findTheBestJobForAllUsers() {
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
