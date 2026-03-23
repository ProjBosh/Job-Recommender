package ru.vk.education.job;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean getOut = false;
        FileService fileService = new FileService();
        Scanner scanner = new Scanner(System.in);

        fileService.executeCommandWithFile();

//        user alice --skills=java,spring --exp=2
//        job DevOps --company=VK --tags=java,ml,spring --exp=2
//        job DevOps2 --company=VK --tags=java --exp=3
//        job DevOps3 --company=VK --tags=java,ml,spring --exp=4
//        suggest alice
//        history


        while(!getOut) {
            String command = scanner.nextLine().trim();
            if(command.equals("exit")) {
                getOut = true;
            } else if(!command.trim().isEmpty()) {
                new CommandParser().execute(command, true);
            }
        }

        scanner.close();
        System.exit(0);
    }
}