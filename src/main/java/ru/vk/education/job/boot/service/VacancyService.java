package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.VacancyRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VacancyService {
    private final VacancyRepository vacancyRepository;

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll().stream()
                .sorted(Comparator.comparing(Vacancy::getJobName))
                .toList();
    }

    public boolean isDuplicate(String jobName, String company, Set<String> tags, int experience) {
        return vacancyRepository.existsByField(jobName, company, tags, experience);
    }

    public boolean isPresent(Vacancy vacancy) {
        if(vacancy == null || vacancy.getId() == null){
            return false;
        }
        return vacancyRepository.isPresent(vacancy.getId());
    }

    public Vacancy getVacancy(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Вакансия в id " + id + " не найдена"));
    }

    public long getTheNumberOfMatchingSkills(Vacancy vacancy, User user) {
        return vacancy.getTags().stream()
                .filter(user.getSkills()::contains)
                .count();
    }

    public Vacancy addVacancy(String jobName, String company, Set<String> tags, int experience) {
        Vacancy vacancy = new Vacancy(null, jobName, company, tags, experience);
        return vacancyRepository.save(vacancy);
    }
}
