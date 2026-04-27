package ru.vk.education.job.boot.domain;

import lombok.*;

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
}
