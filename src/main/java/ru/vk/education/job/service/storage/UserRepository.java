package ru.vk.education.job.service.storage;

import ru.vk.education.job.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {
    private static Set<User> users = new HashSet<>();
    private static Map<String, User> userByName = new HashMap<>();

    public UserRepository() {
        sortCollections();
    }

    public UserRepository(User user) {
        users.add(user);
        userByName.put(user.getFirstName(), user);
        sortCollections();
    }

    public void printList() {
        users.forEach(user -> System.out.println(user.getFirstName() + " " +
                String.join(",", user.getSkills()) + " " +
                user.getExperience()));
    }

    public boolean find(String firstName) { return userByName.containsKey(firstName); }

    public User getUser(String firstName) {
        return userByName.get(firstName);
    }

    private void sortCollections() {
        // Сортируем Set путем создания нового TreeSet
        Set<User> sortedUsers = new TreeSet<>(Comparator.comparing(User::getFirstName));
        sortedUsers.addAll(users);
        users = sortedUsers;

        // Сортируем Map путем создания нового TreeMap
        Map<String, User> sortedMap = new TreeMap<>(userByName);
        userByName = sortedMap;
    }
}
