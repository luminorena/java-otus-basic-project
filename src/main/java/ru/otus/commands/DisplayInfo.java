package ru.otus.commands;

import ru.otus.run.RunCommand.Executable;

import java.io.IOException;
import java.nio.file.Path;


public interface DisplayInfo {
    Executable printWorkingDirectory();

    Executable listAllFiles();

    Executable listAllFilesWithDetails() throws IOException;

    String printFileDetails(Path filePath) throws IOException;

    Boolean findFiles(String fileNameToFind) throws IOException;
}
