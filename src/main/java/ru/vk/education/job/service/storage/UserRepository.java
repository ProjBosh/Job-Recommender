package ru.vk.education.job.service.storage;

import ru.vk.education.job.model.user.User;

import java.util.*;

public class UserRepository {
    private static final List<User> users = new ArrayList<>();
    private static final Map<String, User> userByName = new HashMap<>();

    public UserRepository() {}

    public boolean addUser(User user) {
        if (user == null || user.getFirstName() == null) {
            return false;
        }
        
        // Check if user already exists by name
        if (userByName.containsKey(user.getFirstName())) {
            return false; // User already exists, don't overwrite
        }
        
        users.add(user);
        userByName.put(user.getFirstName(), user);
        return true;
    }

    public void printList() {
        // Sort users alphabetically (case-insensitive for better comparison)
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort((u1, u2) -> {
            String name1 = u1.getFirstName().toLowerCase();
            String name2 = u2.getFirstName().toLowerCase();
            return name1.compareTo(name2);
        });
        
        for (User user : sortedUsers) {
            StringBuilder sb = new StringBuilder();
            Set<String> skills = user.getSkills();
            if (skills != null && !skills.isEmpty()) {
                List<String> sortedSkills = new ArrayList<>(skills);
                Collections.sort(sortedSkills);
                for (int i = 0; i < sortedSkills.size(); i++) {
                    sb.append(sortedSkills.get(i));
                    if (i < sortedSkills.size() - 1) {
                        sb.append(",");
                    }
                }
            }
            System.out.println(user.getFirstName() + " " + sb.toString() + " " + user.getExperience());
        }
    }

    public boolean find(String firstName) { 
        return userByName.containsKey(firstName); 
    }

    public User getUser(String firstName) {
        return userByName.get(firstName);
    }
    
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
}