package ru.vk.education.job.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BestJobSuggestionService {
    private final int periodDelay = 60 * 1000; // 60 секунд
    private final SuggestService suggestService;

    @Scheduled(fixedRate = periodDelay, initialDelay = 0)
    public void findBestVacancy() {
        suggestService.findBestVacancies();
    }
}
