package ru.vk.education.job;

import ru.vk.education.job.cli.CommandParser;
import ru.vk.education.job.service.BestJobSuggestionService;
import ru.vk.education.job.service.FileService;

import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        boolean getOut = false;
        int periodDelay = 60; //60
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        Scanner scanner = new Scanner(System.in);

        // Инициализируем чтение команд из файла и испольнение команд "user ..." и "job ..."
        new FileService().executeCommandWithFile();

        // Фоновый подбор лучшей вакансии для всех пользователей
        StartTheBackgroundProcess(executorService, periodDelay);

        while(!getOut) {
            String command = scanner.nextLine().trim();
            if(command.equals("exit")) {
                getOut = true;
            } else if(!command.trim().isEmpty()) {
                // Инициализация обработки строк и выполнения их действий
                new CommandParser().execute(command, true);
            }
        }

        // При завершение программы
        EndTheBackgroundProcess(executorService, periodDelay);
        scanner.close();
        System.exit(0);
    }

    /**
     * Запуск фонового процесса
     * @param executorService - Сервис исполнитель (Планировщик)
     * @param periodDelay - период ожидания до следующего запуска
     */
    public static void StartTheBackgroundProcess(ScheduledExecutorService executorService, int periodDelay) {
        executorService.scheduleAtFixedRate(
                new BestJobSuggestionService(),
                0,
                periodDelay,
                TimeUnit.SECONDS
        );
    }

    /**
     * Завершение фонового процесса
     * @param executorService - Сервис исполнитель (Планировщик)
     * @param periodDelay - период ожидания до завершения последней принятой задачи
     */
    public static void EndTheBackgroundProcess(ScheduledExecutorService executorService, int periodDelay) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            executorService.shutdown(); // Прекращаем принимать новые задачи
            try {
                // Ожидаем завершения текущих задач
                if (!executorService.awaitTermination(periodDelay, TimeUnit.SECONDS))
                    executorService.shutdownNow(); // Принудительно прерываем
            } catch (InterruptedException e) {
                executorService.shutdown();
            }
        }));
    }
}