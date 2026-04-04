package ru.vk.education.job.model.rating;

import java.util.HashMap;
import java.util.Map;

public class RatingVacancy {
    private double point = 0;
    private String jobName = "";
    private static final Map<String, Double> rating = new HashMap<>();

    public RatingVacancy(final String jobName, final double point){
        add(jobName, point);
    }

    /**
     * Добавить вакансию в рейтинг с количеством совпадающих навыков
     *
     * @param jobName - Название вакансии
     * @param point - Количество совпадающих навыков
     */
    public void add(final String jobName, final double point){
        setJobName(jobName);
        setPoint(point);

        if (!rating.containsKey(jobName))
            rating.put(jobName, point);
    }

    /**
     * Getters and Setters
     */

    public double getPoint() {
        return point;
    }

    public void setPoint(final double point) {
        this.point = point;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }
}
