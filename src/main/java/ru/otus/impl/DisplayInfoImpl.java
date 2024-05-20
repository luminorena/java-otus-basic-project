package ru.otus.impl;

import ru.otus.commands.DisplayInfo;
import ru.otus.run.RunCommand.Executable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class DisplayInfoImpl implements DisplayInfo {


    @Override
    public Executable printWorkingDirectory() {
        return args -> {
            File currentFolder = new File(System.getProperty("user.dir"));
            System.out.println(currentFolder);
        };

    }

    @Override
    public Executable listAllFiles() {
        return args -> {
            File currentFolder = new File(System.getProperty("user.dir"));
            File[] files = currentFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                System.out.println("Папка пуста или не существует");
            }
        };
    }

    @Override
    public Executable listAllFilesWithDetails() {
        return args -> {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(System.getProperty("user.dir")))) {
                for (Path file : stream) {
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    String fileName = file.getFileName().toString();
                    boolean isDirectory = attrs.isDirectory();
                    FileTime lastModifiedTime = attrs.lastModifiedTime();


                    // Преобразование FileTime в Date
                    Date lastModifiedDate = new Date(lastModifiedTime.toMillis());

                    // Форматирование даты в нужный формат
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = sdf.format(lastModifiedDate);
                    long size = isDirectory ? getDirSize(file) : attrs.size();
                    System.out.println(fileName + " " + isDirectory + " " + size + " " + formattedDate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }


    public long getDirSize(Path path) throws IOException {
        AtomicLong size = new AtomicLong(0);
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size.addAndGet(attrs.size());
                return FileVisitResult.CONTINUE;
            }
        });
        return size.get();
    }

    @Override
    public String printFileDetails(Path filePath) throws IOException {
        StringBuilder details = new StringBuilder();
        Map<String, Object> attributes = Files.readAttributes(filePath, "*");
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            if (entry.getKey().equals("size")) {
                details.append(entry.getKey()).append(":").append(entry.getValue()).append("B\n");
            } else {
                details.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
        }
        return details.toString();
    }


    @Override
    public Boolean findFiles(String fileNameToFind) throws IOException {
        AtomicBoolean result = new AtomicBoolean();
        result.set(false);
        Files.walkFileTree(Path.of(System.getProperty("user.dir")), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.getFileName().toString().equals(fileNameToFind)) {
                    result.set(true);
                    System.out.println("File found: " + file.toAbsolutePath());
                    return FileVisitResult.TERMINATE;
                } else
                    result.set(false);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (dir.getFileName().toString().equals(fileNameToFind)) {
                    System.out.println("Directory found: " + dir.toAbsolutePath());
                    result.set(true);
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
        if (!result.get()) {
            System.out.println("Nothing found");
        }

        return result.get();

    }
}



