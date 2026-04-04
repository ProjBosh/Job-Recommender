package ru.vk.education.job.service;

import ru.vk.education.job.cli.CommandParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private final Path path;

    /**
     * Проверяет существование пути к файлу. Добавляет при отстутствии
     */
    public FileService() {
        String FILE_WITH_COMMAND = "File with command.txt";
        this.path = Paths.get(FILE_WITH_COMMAND);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (Exception e) {
            // Игнорируем
        }
    }

    /**
     * Сохраняет корректную и выполненную команду в файл
     *
     * @param command - Входная команда
     */
    public void saveCommand(final String command) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path, StandardOpenOption.APPEND), StandardCharsets.UTF_8))) {
            bw.write(command);
            bw.newLine();
        } catch (IOException e) {
            // Игнорируем
        }
    }

    /**
     * Выполняет команды "user ..." и "job ..." при чтение из файла
     */
    public void executeCommandWithFile() {
        List<String> commandsWithFile = getCommand();

        if (commandsWithFile.isEmpty())
            return;

        CommandParser cmdParser = new CommandParser();

        for (String cmd : commandsWithFile)
            if (cmd.startsWith("user ") || cmd.startsWith("job "))
                cmdParser.execute(cmd, false);
    }

    /**
     * Печатает все команды, которые находятся в файле
     */
    public void printlnCommandWithFile() {
        List<String> commandsWithFile = getCommand();

        if (commandsWithFile.isEmpty())
            return;

        for (String cmd : commandsWithFile)
            System.out.println(cmd);
    }

    /**
     * Возвращает пользователю команду из файла
     * @return Команда из файла
     */
    private List<String> getCommand() {
        List<String> commands = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))) {
            String line;

            while ((line = br.readLine()) != null)
                commands.add(line.trim());
        } catch (Exception e) {
            // Игнорируем
        }

        return commands;
    }
}
