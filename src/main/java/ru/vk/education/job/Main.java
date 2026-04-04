package ru.vk.education.job;

import ru.vk.education.job.cli.CommandParser;
import ru.vk.education.job.model.repository.RecommendationRepository;
import ru.vk.education.job.service.FileService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean getOut = false;
        Scanner scanner = new Scanner(System.in);

        // Инициализируем чтение команд из файла и испольнение команд "user ..." и "job ..."
        new FileService().executeCommandWithFile();
        // Инициализируем заполнение рейтинга совпадений у пользователей по вакансиям
        new RecommendationRepository().init();

        while(!getOut) {
            String command = scanner.nextLine().trim();
            if(command.equals("exit")) {
                getOut = true;
            } else if(!command.trim().isEmpty()) {
                // Инициализация обработки строк и выполнения их действий
                new CommandParser().execute(command, true);
            }
        }

        scanner.close();
        System.exit(0);
    }
}