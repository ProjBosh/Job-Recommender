package ru.vk.education.job.model;

import java.util.*;

public class Vacancy {
    private String company;
    private String jobName;
    private Set<String> tags = new HashSet<>();
    private int experience;
    private static final List<Vacancy> vacancies = new ArrayList<>();
    private static final Map<String, Vacancy> vacanciesByName = new HashMap<>();

    public Vacancy() {}

    public void add(String jobName, String company, Set<String> tags, int experience) {
        setJobName(jobName);
        setCompany(company);
        setTags(tags);
        setExperience(experience);

        vacancies.add(this);
        vacancies.sort(Comparator.comparing(Vacancy::getJobName));
        vacanciesByName.put(jobName, this);
    }

    public void println() {
        for (Vacancy v : vacancies) {
            System.out.println(v.getJobName() + " at " + v.getCompany());
        }
    }

    public boolean isPresent(String jobName) {
        return !jobName.trim().isEmpty() && vacanciesByName.containsKey(jobName);
    }

    public int findTags(String tag){
        return (tags.contains(tag) ? 1 : 0);
    }

    public String getCompany() {
        return company;
    }

    private void setCompany(String company) {
        this.company = company;
    }

    public String getJobName() {
        return jobName;
    }

    private void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getTagsToString() {
        return String.join(",", getTags());
    }

    private void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public int getExperience() {
        return experience;
    }

    private void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Vacancy> getVacancies(){
        return vacancies;
    }
}
