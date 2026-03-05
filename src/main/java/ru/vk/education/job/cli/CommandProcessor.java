package ru.vk.education.job.cli;

import java.util.Scanner;

public class CommandProcessor {
    public void split(String inputLine) {
        String[] parts = inputLine.split(" ");
        String command = parts[0];

        if (command.equals("user")) {
            parseCommandUser(parts);
        } else if (command.equals("user-list")) {

        }
    }
    /*
        Разбиение команды user <name> --skills=... --exp=... на компоненты
        Добавление пользователя в системы
     */
    private void parseCommandUser(String[] parts) {
        String username = parts[1];
        String[] skills = new String[0];

        int experience = 0;

        if (username != null && !username.trim().isEmpty()) {
            try {
//                System.out.println("Имя пользователя: " + username);
                for (int i = 2; i < parts.length; i++) {
                    String param = parts[i];    // например, --skills=java,ml,linux

                    // Убираем "--"
                    String withoutPrefix = param.substring(2);

                    // Разбиваем по "="
                    String[] keyValue = withoutPrefix.split("=");
                    String key = keyValue[0];       // "skills"
                    String value = keyValue[1];     // "java,ml,linux"

                    if (key.equals("skills")) {
                        skills = value.split(",");
                    } else if (key.equals("exp")) {
                        experience = Integer.parseInt(value);
                    }
                }

                // TODO: Реализовать заполнение данных в класс User

                /*
                 * Демонстрационный вывод данных в консоль для тестирования
                    System.out.print(username + " ");
                    for (String skill : skills) {
                        System.out.print(skill + " ");
                    }
                    System.out.println(experience);
                 */
            } catch (Exception e) {
                System.err.println("Ошибка при обработке имени: " + e.getMessage());
            }
        } else {
            try {
                throw new IllegalArgumentException("Имя пользователя не может быть пустым");
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка валидации: " + e.getMessage());
            }
        }
    }
}
