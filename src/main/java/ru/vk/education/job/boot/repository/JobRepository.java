package ru.vk.education.job.boot.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.vk.education.job.boot.domain.Vacancy;

import java.sql.Array;
import java.util.*;

@Repository
public class JobRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Vacancy> vacancyRowMapper = (rs, rowNum) -> {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(UUID.fromString(rs.getString("id")));
        vacancy.setJobName(rs.getString("job_name"));
        vacancy.setCompany(rs.getString("company"));
        vacancy.setExperience(rs.getInt("experience"));

        Array tagsArray = rs.getArray("tags");
        Set<String> tags = new HashSet<>();
        if (tagsArray != null) {
            String[] tagsArr = (String[]) tagsArray.getArray();
            for (String tag : tagsArr) {
                if (tag != null) {
                    tags.add(tag);
                }
            }
        }
        vacancy.setTags(tags);
        return vacancy;
    };

    public JobRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Vacancy vacancy) {
        String vacancySql = """
            INSERT INTO jobs (id, job_name, company, experience) 
            VALUES (:id, :jobName, :company, :experience) ON CONFLICT (id) DO UPDATE SET job_name = :jobName, company = :company, experience = :experience
        """;

        SqlParameterSource vacancyParams = new BeanPropertySqlParameterSource(vacancy);
        jdbcTemplate.update(vacancySql, vacancyParams);

        String deleteSkillsSql = "DELETE FROM job_skills WHERE job_id = :jobId";
        jdbcTemplate.update(deleteSkillsSql, Map.of("jobId", vacancy.getId()));

        if (vacancy.getTags() != null && !vacancy.getTags().isEmpty()) {
            String skillSql = "INSERT INTO skills (name) VALUES (:name) ON CONFLICT (name) DO NOTHING";
            for (String tagName : vacancy.getTags()) {
                jdbcTemplate.update(skillSql, Map.of("name", tagName));
            }

            String jobSkillSql = """
                INSERT INTO job_skills (job_id, skill_id)
                SELECT :jobId, id
                FROM skills WHERE name = :tagName
            """;
            for (String tagName : vacancy.getTags()) {
                jdbcTemplate.update(jobSkillSql, Map.of("jobId", vacancy.getId(), "tagName", tagName));
            }
        }
    }

    public Optional<Vacancy> findById(UUID id) {
        String sql = """
            SELECT j.id, j.job_name, j.company, j.experience, array_agg(s.name) as tags
            FROM jobs j
            LEFT JOIN job_skills js ON j.id = js.job_id
            LEFT JOIN skills s ON js.skill_id = s.id
            WHERE j.id = :id
            GROUP BY j.id, j.job_name, j.company, j.experience
        """;

        try {
            Vacancy vacancy = jdbcTemplate.queryForObject(sql, Map.of("id", id), vacancyRowMapper);
            return Optional.ofNullable(vacancy);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Vacancy> findAll() {
        String sql = """
            SELECT j.id, j.job_name, j.company, j.experience, array_agg(s.name) as tags
            FROM jobs j
            LEFT JOIN job_skills js ON j.id = js.job_id
            LEFT JOIN skills s ON js.skill_id = s.id
            GROUP BY j.id, j.job_name, j.company, j.experience
        """;

        return jdbcTemplate.query(sql, vacancyRowMapper);
    }
}