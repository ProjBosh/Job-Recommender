package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.JobRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VacancyService {
    private final JobRepository jobRepository;

    public List<Vacancy> getAllVacancies() {
        return null;
//        return vacancyRepository.findAll().stream()
//                .sorted(Comparator.comparing(Vacancy::getJobName))
//                .toList();
    }

    public boolean isDuplicate(String jobName, String company, Set<String> tags, int experience) {
        return false;
//        return vacancyRepository.existsByField(jobName, company);
    }

    public Vacancy getVacancy(Long id) {
        return null;
//        return vacancyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Вакансия в id " + id + " не найдена"));
    }

    /**
     * Получить количество совпадающих навыков у пользователя для вакансии
     *
     * @param vacancy - вакансия
     * @param user - пользователь
     * @return Количество совпадающих навыков
     */
    public long getTheNumberOfMatchingSkills(Vacancy vacancy, User user) {
        return 0;
//        return vacancy.getTags().stream()
//                .filter(user.getSkills()::contains)
//                .count();
    }

    public Vacancy create(String jobName, String company, Set<String> tags, int experience) {
        return null;
//        Vacancy vacancy = new Vacancy(null, jobName, company, tags, experience);
//        return vacancyRepository.save(vacancy);
    }
}
