package ru.otus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.otus.impl.ChangeDataImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeDataTests {
    ChangeDataImpl changeData = new ChangeDataImpl();

    @DisplayName("Current directory has been changed")
    @Test
    public void changeCurrentDirTest(@TempDir Path tempDir) {
        String initialPath = System.getProperty("user.dir");
        changeData.changeCurrentDirectory(tempDir);
        String endPath = System.getProperty("user.dir");
        Assertions.assertNotEquals(initialPath, endPath);
    }

    @DisplayName("Current directory has been changed to the parent one")
    @Test
    public void goToParentDirTest() throws IOException, InterruptedException {
        String initialDirectory = System.getProperty("user.dir");
        changeData.goToParentDirectory().execute();
        String newDirectory = System.getProperty("user.dir");

        Path initialPath = Paths.get(initialDirectory);
        Path newPath = Paths.get(newDirectory);
        assertTrue(newPath.startsWith(initialPath.getParent()));

    }

    @DisplayName("Files are deleted successfully")
    @Test
    public void deleteFilesTest(@TempDir Path tempDir1) throws IOException {
        Path test1 = tempDir1.resolve("test1.txt");
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(test1, lines);
        changeData.deleteFiles(test1);
        assertTrue(Files.notExists(test1));

    }

}
