package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.UserRepository;
import ru.vk.education.job.boot.repository.VacancyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;

    public List<Vacancy> getMatchesVacancy(@NotNull User user, @NotNull Integer minExperience) {
        return vacancyRepository.findAll().stream()
                .filter(v -> v.getExperience() >= minExperience)
                .toList();
    }
}
