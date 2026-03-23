package ru.vk.education.job.model;

import java.util.*;

public class User {
    private String firstName;
    private Set<String> skills = new HashSet<>();
    private int experience;
    private static final List<User> users = new ArrayList<>();
    private static final Map<String, User> usersByName = new HashMap<>();

    public User() {}

    public void add(String firstName, Set<String> skills, int experience) {
        setFirstName(firstName);
        setSkills(skills);
        setExperience(experience);

        users.add(this);
        users.sort(Comparator.comparing(User::getFirstName));
        usersByName.put(firstName, this);
    }

    public void println() {
        for (User u : users) {
            System.out.println(u.getFirstName() + " " + u.getSkillsToString() + " " + u.getExperience());
        }
    }

    public boolean isPresent(String firstName) {
        return !firstName.trim().isEmpty() && usersByName.containsKey(firstName);
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getExperience() {
        return experience;
    }

    private void setExperience(int experience) {
        this.experience = experience;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public String getSkillsToString() {
        return String.join(",", getSkills());
    }

    private void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public User getUser(String firstName) {
        return usersByName.get(firstName);
    }
}
