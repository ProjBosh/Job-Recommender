package ru.vk.education.job.boot.service;

import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.education.job.boot.domain.User;
import ru.vk.education.job.boot.domain.Vacancy;
import ru.vk.education.job.boot.repository.JobRepository;
import ru.vk.education.job.boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuggestServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VacancyService vacancyService;

    @InjectMocks
    private SuggestService suggestService;

    private final String TEST_USER_NAME = "John";
    private final UUID TEST_USER_ID = UUID.randomUUID();

    /**
     * Сценарий 1: suggestTest — обычный сценарий, когда в системе несколько вакансий и один пользователь
     */
    @Test
    void suggestTest() {
        // Given
        User user = createUser(TEST_USER_NAME, 5, Set.of("Java", "Spring", "SQL"));
        Vacancy vacancy1 = createVacancy("Java Developer", "Company A", 3, Set.of("Java", "Spring", "SQL"));
        Vacancy vacancy2 = createVacancy("Python Developer", "Company B", 2, Set.of("Python", "Django"));
        Vacancy vacancy3 = createVacancy("Senior Java", "Company C", 5, Set.of("Java", "Spring", "Microservices"));

        when(userRepository.findByFirstName(TEST_USER_NAME)).thenReturn(Optional.of(user));
        when(jobRepository.findAll()).thenReturn(List.of(vacancy1, vacancy2, vacancy3));

        // Настройка совпадающих навыков
        when(vacancyService.getTheNumberOfMatchingSkills(vacancy1, user)).thenReturn(3L);
        when(vacancyService.getTheNumberOfMatchingSkills(vacancy2, user)).thenReturn(0L);
        when(vacancyService.getTheNumberOfMatchingSkills(vacancy3, user)).thenReturn(2L);

        // When
        List<Vacancy> result = suggestService.suggest(TEST_USER_NAME);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size()); // DEFAULT_VACANCY_COUNT = 2
        assertEquals("Java Developer", result.get(0).getJobName());
        assertEquals("Senior Java", result.get(1).getJobName());
    }

    /**
     * Сценарий 2: emptyVacanciesTest — когда в системе нет вакансий
     */
    @Test
    void emptyVacanciesTest() {
        // Given
        User user = createUser(TEST_USER_NAME, 5, Set.of("Java", "Spring"));

        when(userRepository.findByFirstName(TEST_USER_NAME)).thenReturn(Optional.of(user));
        when(jobRepository.findAll()).thenReturn(List.of()); // Пустой список вакансий

        // When
        List<Vacancy> result = suggestService.suggest(TEST_USER_NAME);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Сценарий 3: singleVacancyTest — когда в системе только одна вакансия
     */
    @Test
    void singleVacancyTest() {
        // Given
        User user = createUser(TEST_USER_NAME, 3, Set.of("Java", "Spring", "Hibernate"));
        Vacancy vacancy = createVacancy("Java Engineer", "Tech Corp", 2, Set.of("Java", "Spring", "Hibernate"));

        when(userRepository.findByFirstName(TEST_USER_NAME)).thenReturn(Optional.of(user));
        when(jobRepository.findAll()).thenReturn(List.of(vacancy));
        when(vacancyService.getTheNumberOfMatchingSkills(vacancy, user)).thenReturn(3L);

        // When
        List<Vacancy> result = suggestService.suggest(TEST_USER_NAME);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Engineer", result.get(0).getJobName());
        assertEquals("Tech Corp", result.get(0).getCompany());
    }

    /**
     * Сценарий 4: userNotFoundTest — пользователь не найден в системе
     */
    @Test
    void userNotFoundTest() {
        // Given
        String nonExistentUser = "NonExistentUser";

        when(userRepository.findByFirstName(nonExistentUser)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> suggestService.suggest(nonExistentUser));

        assertTrue(exception.getMessage().contains("User not found"));
        assertTrue(exception.getMessage().contains(nonExistentUser));
    }

    // Вспомогательные методы для создания тестовых объектов
    private User createUser(String firstName, int experience, Set<String> skills) {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setFirstName(firstName);
        user.setExperience(experience);
        user.setSkills(skills);
        return user;
    }

    private Vacancy createVacancy(String jobName, String company, int experience, Set<String> tags) {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(UUID.randomUUID());
        vacancy.setJobName(jobName);
        vacancy.setCompany(company);
        vacancy.setExperience(experience);
        vacancy.setTags(tags);
        return vacancy;
    }
}