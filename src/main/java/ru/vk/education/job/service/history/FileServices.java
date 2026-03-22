package ru.vk.education.job.service.history;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileServices {
    private static final String FILE_WITH_COMMANDS = "commands.txt";
    private final Path path;

    public FileServices() {
        this.path = Paths.get(FILE_WITH_COMMANDS);
        initializeFile();
    }

    private void initializeFile() {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            // Игнорируем ошибки
        }
    }

    /**
     * Сохраняет команду в файл (ВСЕ команды, кроме exit)
     * Сохраняем ВСЕ команды, включая дубликаты
     */
    public void saveCommand(String command) {
        // Проверяем, не является ли команда exit
        if (command.equals("exit")) {
            return;
        }
        
        // Сохраняем ВСЕ команды, включая дубликаты (убираем проверку isCommandDuplicate)
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        Files.newOutputStream(path, StandardOpenOption.APPEND),
                        StandardCharsets.UTF_8
                )
        )) {
            writer.write(command);
            writer.newLine();
        } catch (IOException e) {
            // Игнорируем ошибки
        }
    }

    /**
     * Получает список всех команд из файла
     */
    public List<String> getListCommand() {
        List<String> commands = new ArrayList<>();

        if (!Files.exists(path)) {
            return Collections.emptyList();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                commands.add(line.trim());
            }
        } catch (IOException e) {
            // Игнорируем ошибки
        }

        return commands;
    }

    /**
     * Проверяет, является ли команда командой создания сущности
     */
    public boolean isEntityCreationCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String lowerCaseCommand = command.toLowerCase().trim();
        return lowerCaseCommand.startsWith("user ") ||
               lowerCaseCommand.startsWith("job ");
    }

    /**
     * Выводит список команд в консоль (точный формат для history)
     */
    public void printListCommand(List<String> commands) {
        for (String command : commands) {
            System.out.println(command);
        }
    }
}