package ru.vk.education.job.boot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private Set<String> skills = new HashSet<>();
    private int experience;

    public String getSkillsToString() {
        return String.join(",", getSkills());
    }
}
