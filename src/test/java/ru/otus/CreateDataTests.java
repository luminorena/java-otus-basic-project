package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.otus.impl.CreateDataImpl;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.Files.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateDataTests {
    CreateDataImpl createData = new CreateDataImpl();

    @DisplayName("Directory created successfully")
    @Test
    public void createDirectoryTest() throws IOException {
        String directoryName = "testDirectory";
        createDirectory(Path.of(directoryName));
        assertTrue(Files.exists(Path.of(directoryName)), "Directory should have been created");
        deleteIfExists(Path.of(directoryName));
    }

    @DisplayName("Existing directory is not created")
    @Test
    public void createExistingDirectoryTest() {
        String directoryName = "existingDirectory";
        try {
            createDirectory(Path.of(directoryName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThrows(FileAlreadyExistsException.class, () -> {
            createDirectory(Path.of(directoryName));
        });

    }

    @DisplayName("File created successfully")
    @Test
    public void createFileTest() throws IOException {
        String fileName = "test1.txt";
        createFile(Path.of(fileName));
        assertTrue(Files.exists(Path.of(fileName)), "File should have been created");
        deleteIfExists(Path.of(fileName));
    }

    @DisplayName("Existing file is not created")
    @Test
    public void createExistingFileTest() {
        String fileName = "test.txt";
        try {
            createFile(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThrows(FileAlreadyExistsException.class, () -> {
            createDirectory(Path.of(fileName));
        });
    }


    @DisplayName("Files with directories are moved")
    @Test
    public void moveFilesTest(@TempDir Path tempDir1, @TempDir Path tempDir2) throws IOException {
        Path test1 = tempDir1.resolve("test1.txt");
        Path test2 = tempDir2.resolve("test2.txt");
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(test1, lines);
        Files.write(test2, lines);
        createData.moveFiles(test1, test2);
        assertAll(
                () -> assertTrue(Files.notExists(test1)),
                () -> assertTrue(Files.exists(test2)),
                () -> assertTrue(Files.isDirectory(tempDir2.resolve(tempDir1))));
    }

    @DisplayName("Files are copied")
    @Test
    public void copyFilesTest(@TempDir Path tempDir1, @TempDir Path tempDir2) throws IOException {
        Path file1 = tempDir1.resolve("test1.txt");
        Path file2 = tempDir2.resolve("test2.txt");
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(file1, lines);
        Files.write(file2, lines);
        createData.copyFiles(file1, file2);
        assertTrue(Files.isDirectory(tempDir2.resolve(tempDir1)));

    }
}
