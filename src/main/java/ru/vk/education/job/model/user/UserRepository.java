package ru.vk.education.job.model.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserRepository {
    private static final Set<User> users = new HashSet<>();
    private static final Map<String, User> userByName = new HashMap<>();

    public UserRepository() {}

    public UserRepository(User user) {
        users.add(user);
        userByName.put(user.getFirstName(), user);
    }

    public void printList() {
        for (User user : users) {
            System.out.println(user.getFirstName() + " " +
                            String.join(",", user.getSkills()) + " " +
                            user.getExperience());
        }
    }

    public boolean find(String firstName) {
        return userByName.containsKey(firstName);
    }
}
