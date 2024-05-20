package ru.otus.commands;

import ru.otus.run.RunCommand.Executable;

import java.io.IOException;
import java.nio.file.Path;

public interface ChangeData {
    void changeCurrentDirectory(Path filePath);

    Executable goToParentDirectory();

    void deleteFiles(Path path) throws IOException;
}
