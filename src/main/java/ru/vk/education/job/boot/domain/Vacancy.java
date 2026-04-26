package ru.vk.education.job.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    private Long id;
    private String jobName;
    private String company;
    private Set<String> tags = new HashSet<>();
    private int experience;

    public String getSkillsToString() {
        return String.join(",", getTags());
    }
}
