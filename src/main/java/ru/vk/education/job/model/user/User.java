package ru.vk.education.job.model.user;

import ru.vk.education.job.service.storage.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String firstName;
    private Set<String> skills = new HashSet<>();
    private int experience;

    public User() {}

    public User(String firstName, Set<String> skills, int experience) {
        if (nameIsEnteredCorrectly(firstName)) {
            this.firstName = firstName;
            this.skills = skills != null ? new HashSet<>(skills) : new HashSet<>();
            this.experience = experience;
            // Add to storage
            new UserRepository().addUser(this);
        }
    }

    public boolean nameIsEnteredCorrectly(String userFirstName) {
        return (userFirstName != null && !userFirstName.trim().isEmpty() && !userFirstName.contains("--"));
    }

    public String getFirstName() {
        return firstName;
    }

    public Set<String> getSkills() {
        return new HashSet<>(skills);
    }

    public int getExperience() {
        return experience;
    }
}