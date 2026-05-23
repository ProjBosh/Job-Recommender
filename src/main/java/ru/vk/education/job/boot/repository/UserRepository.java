package ru.vk.education.job.boot.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.vk.education.job.boot.domain.User;

import java.sql.Array;
import java.util.*;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setFirstName(rs.getString("first_name"));
        user.setExperience(rs.getInt("experience"));

        Array skillsArrayWithResultSet = rs.getArray("skills");
        Set<String> skills = new HashSet<>();
        if (skillsArrayWithResultSet != null) {
            String[] skillsArr = (String[]) skillsArrayWithResultSet.getArray();
            for (String skill : skillsArr) {
                if(skill != null) {
                    skills.add(skill);
                }
            }
        }
        user.setSkills(skills);
        return user;
    };

    public UserRepository (NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String userSql = """
            INSERT INTO users (id, first_name, experience) 
            VALUES (:id, :firstName, :experience) ON CONFLICT (id) DO UPDATE SET first_name = :firstName, experience = :experience
        """;

        SqlParameterSource userParams = new BeanPropertySqlParameterSource(user);
        jdbcTemplate.update(userSql, userParams);

        String deleteSkillsSql = "DELETE FROM user_skills WHERE user_id = :userId";
        jdbcTemplate.update(deleteSkillsSql, Map.of("userId", user.getId()));

        if (user.getSkills() != null && !user.getSkills().isEmpty()) {
            String skillSql = "INSERT INTO skills (name) VALUES (:name) ON CONFLICT (name) DO NOTHING";
            for (String skillName : user.getSkills()) {
                jdbcTemplate.update(skillSql, Map.of("name", skillName));
            }

            String userSkillSql = """
                INSERT INTO user_skills (user_id, skill_id)
                SELECT :userId, id
                FROM skills WHERE name = :skillName
            """;
            for (String skillName : user.getSkills()) {
                jdbcTemplate.update(userSkillSql, Map.of("userId", user.getId(), "skillName", skillName));
            }
        }
    }

    public Optional<User> findById(UUID id) {
        String sql = """
            SELECT u.id, u.first_name, u.experience, array_agg(s.name) as skills
            FROM users u
            LEFT JOIN user_skills us ON u.id = us.user_id
            LEFT JOIN skills s ON us.skill_id = s.id
            WHERE u.id = :id
            GROUP BY u.id, u.first_name, u.experience
        """;

        try {
            User user = jdbcTemplate.queryForObject(sql, Map.of("id", id), userRowMapper);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByFirstName(String userName) {
        String sql = """
            SELECT u.first_name
            FROM users u
            WHERE u.first_name = :userName
        """;

        try {
            User user = jdbcTemplate.queryForObject(sql, Map.of("userName", userName), userRowMapper);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        String sql = """
                SELECT u.id, u.first_name, u.experience, array_agg(s.name) as skills
                FROM users u
                LEFT JOIN user_skills us ON u.id = us.user_id
                LEFT JOIN skills s ON us.skill_id = s.id
                GROUP BY u.id, u.first_name, u.experience
        """;

        return jdbcTemplate.query(sql, userRowMapper);
    }
}
