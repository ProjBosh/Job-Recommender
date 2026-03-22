package ru.vk.education.job;

import ru.vk.education.job.cli.CommandProcessor;
import ru.vk.education.job.service.history.FileServices;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileServices fs = new FileServices();
        
        // HomeWork_2

//        user alice --skills=java,ml,linux --exp=2
//        user bob --exp=10 --skills=java,java,java
//        user-list
//        job Backend_Dev --company=VK --tags=java,backend,linux --exp=1
//        job Backend_Dev2 --company=Avito --tags=java,backend,linux --exp=1
//        job-list

        // Выполняем ВСЕ команды из файла при запуске (только создающие сущности)
        List<String> listStartupCommand = fs.getListCommand();//getStartupCommands();
        for (String command : listStartupCommand) {
            if (fs.isEntityCreationCommand(command)) {
                new CommandProcessor().split(command);
            } else {
                fs.saveCommand(command);
            }
        }

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            if (!command.isEmpty()) {
                new CommandProcessor().split(command);
            }
        }

        scanner.close();
        System.exit(0);
    }
}