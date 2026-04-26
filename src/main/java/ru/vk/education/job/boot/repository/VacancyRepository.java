package ru.vk.education.job.boot.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.vk.education.job.boot.domain.Vacancy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRepository {
    private final Map<Long, Vacancy> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public boolean existsByField(String jobName, String company, Set<String> tags, int experience) {
        Set<String> safeTags = tags != null ? tags : Set.of();

        return storage.values().stream()
                .anyMatch(vacancy ->
                        vacancy.getJobName().equals(jobName) &&
                        vacancy.getCompany().equals(company) &&
                        vacancy.getTags().equals(safeTags) &&
                        vacancy.getExperience() == experience
                );
    }


    public boolean isPresent(Long id) {
        return storage.containsKey(id);
    }

    public Optional<Vacancy> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Vacancy> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Vacancy save(Vacancy vacancy) {
        if (vacancy.getId() == null){
            vacancy.setId(idGenerator.getAndIncrement());
        }
        storage.put(vacancy.getId(), vacancy);
        return vacancy;
    }
}
