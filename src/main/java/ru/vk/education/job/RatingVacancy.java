package ru.vk.education.job;

import java.util.HashMap;
import java.util.Map;

public class RatingVacancy {
    private double point = 0;
    private String jobName = "";
    private final Map<String, Double> rating = new HashMap<>();

    public RatingVacancy(String jobName, double point){
        add(jobName, point);
    }

    public void add(String jobName, double point){
        setJobName(jobName);
        setPoint(point);
        if (!rating.containsKey(jobName))
            rating.put(jobName, point);
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
