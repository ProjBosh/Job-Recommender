package ru.vk.education.job.service;

import ru.vk.education.job.cli.RatingVacancy;
import ru.vk.education.job.model.User;
import ru.vk.education.job.model.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class RecommendationService {

    public RecommendationService() {}

    public void suggestVacancy(String firstName) {
        User user = new User().getUser(firstName);
        List<Vacancy> vacancies = new Vacancy().getVacancies();
        ArrayList<RatingVacancy> ratingVacancies = new ArrayList<>();

        for(Vacancy v : vacancies) {
            double points = 0;
            double point = 0;

            for (String skill : user.getSkills())
                point += v.findTags(skill);

            points += (point > 0 && user.getExperience() >= v.getExperience() ? point : (point / 2));
            if (points > 0)
                ratingVacancies.add(new RatingVacancy(v.getJobName(), points));
        }
        ratingVacancies.sort((a, b) -> Double.compare(b.getPoint(), a.getPoint()));

        int count = 0;
        for (RatingVacancy rv : ratingVacancies) {
            if (count == 2)
                break;

            System.out.println(rv.getJobName() + " " + rv.getPoint());
            count++;
        }
    }
}

