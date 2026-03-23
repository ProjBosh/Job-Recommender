package ru.vk.education.job;

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

    public void saveCommand(String command) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path, StandardOpenOption.APPEND), StandardCharsets.UTF_8))) {
            bw.write(command);
            bw.newLine();
        } catch (IOException e) {
            // Игнорируем
        }
    }

    public void executeCommandWithFile() {
        List<String> commandsWithFile = getCommand();

        if (commandsWithFile.isEmpty())
            return;

        CommandParser cmdParser = new CommandParser();

        for (String cmd : commandsWithFile)
            if (cmd.startsWith("user ") || cmd.startsWith("job "))
                cmdParser.execute(cmd, false);
    }

    public void printlnCommandWithFile() {
        List<String> commandsWithFile = getCommand();

        if (commandsWithFile.isEmpty())
            return;

        for (String cmd : commandsWithFile)
            System.out.println(cmd);
    }

    private List<String> getCommand() {
        List<String> commands = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))) {
            String line = "";

            while ((line = br.readLine()) != null) {
                commands.add(line.trim());
                if (line == null)
                    return new ArrayList<String>();
            }
        } catch (Exception e) {
            // Игнорируем
        }

        return commands;
    }
}
