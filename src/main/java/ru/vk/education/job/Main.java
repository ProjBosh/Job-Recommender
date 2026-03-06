package ru.vk.education.job;

import ru.vk.education.job.cli.CommandProcessor;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandProcessor commandProcessor = new CommandProcessor();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("Input command: ");
            String command = scanner.nextLine();
            if (command.equals("exit")) { break; }
            commandProcessor.split(command);
        }

        scanner.close();
        System.exit(0);
    }
}