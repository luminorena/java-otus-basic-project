package ru.otus.commands;


import java.io.IOException;
import java.nio.file.Path;

public interface CreateData {
    void createDirectory(String directoryName);

    void createFile(String fileName);

    void moveFiles(Path source, Path destination) throws IOException;

    void copyFiles(Path source, Path destination) throws IOException;
}
