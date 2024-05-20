package ru.otus.impl;

import ru.otus.commands.ChangeData;
import ru.otus.run.RunCommand.Executable;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ChangeDataImpl implements ChangeData {
    @Override
    public void changeCurrentDirectory(Path filePath) {
        try {
            System.setProperty("user.dir", String.valueOf(filePath.toAbsolutePath()));
            System.out.println(System.getProperty("user.dir"));
        } catch (Exception e) {
            System.out.println("Error changing directory: " + e.getMessage());
        }
    }

    @Override
    public Executable goToParentDirectory() {
        return args -> {
            String directoryPath = System.getProperty("user.dir");
            Path targetDirectory = Paths.get(directoryPath).getParent();
            try {
                System.setProperty("user.dir", targetDirectory.toString());
                System.out.println(targetDirectory.toAbsolutePath());
            } catch (NullPointerException e) {
                System.out.println("The path does not have a parent directory");
            }
        };
    }

    @Override
    public void deleteFiles(Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    if (!Files.exists(file))
                        System.out.println("File deleted");
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println("File does not exist. Impossible to delete");
        }

    }
}
