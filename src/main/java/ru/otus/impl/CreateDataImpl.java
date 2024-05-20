package ru.otus.impl;

import ru.otus.commands.CreateData;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class CreateDataImpl implements CreateData {
    @Override
    public void createDirectory(String directoryName) {
        if (!Files.exists(Path.of(directoryName))) {
            try {
                Files.createDirectory(Path.of(directoryName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else System.out.println("Directory already exists");
    }

    @Override
    public void createFile(String fileName) {
        if (!Files.exists(Path.of(fileName))) {
            try {
                Files.createFile(Path.of(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else System.out.println("File already exists");
    }

    @Override
    public void moveFiles(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path newDestination = destination.resolve(source.relativize(file));
                Files.move(file, newDestination, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path newDestination = destination.resolve(source.relativize(dir));
                Files.createDirectories(newDestination);
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println("Successful");

    }


    @Override
    public void copyFiles(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path newDir = destination.resolve(source.relativize(dir));
                Files.createDirectories(newDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path newFile = destination.resolve(source.relativize(file));
                Files.createDirectories(newFile.getParent()); // Создаем родительские директории для файла
                Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println("Copied");
    }

}
