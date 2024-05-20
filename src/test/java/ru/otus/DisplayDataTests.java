package ru.otus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.otus.impl.DisplayInfoImpl;
import ru.otus.run.RunCommand.Executable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DisplayDataTests {
    DisplayInfoImpl displayInfo = new DisplayInfoImpl();

    private static String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC+3"));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    @DisplayName("Working directory is printed")
    @Test
    public void printWorkingDirTest() throws IOException, InterruptedException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        Executable executable = displayInfo.printWorkingDirectory();
        executable.execute();

        String expectedOutput = new File(System.getProperty("user.dir")).getAbsolutePath();
        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
    }

    @DisplayName("All files of current directory are listed to console")
    @Test
    public void listAllFilesTest() throws IOException, InterruptedException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        Executable executable = displayInfo.listAllFiles();
        executable.execute();

        String expectedOutput = Arrays.toString(Objects.requireNonNull(new File(System.getProperty("user.dir")).listFiles()));

        expectedOutput = expectedOutput.replaceAll("\\[|\\]", "");
        String[] paths = expectedOutput.split(", ");

        List<String> names = new ArrayList<>();
        for (String path : paths) {
            names.add(path.substring(path.lastIndexOf("\\") + 1).trim());
        }

        String actualOutput = outputStreamCaptor.toString().trim();
        String[] actualPaths = actualOutput.split(System.lineSeparator());

        List<String> actualNames = Arrays.stream(actualPaths)
                .map(path -> path.substring(path.lastIndexOf(File.separator) + 1).trim())
                .collect(Collectors.toList());

        assertEquals(names, actualNames);
    }

    @DisplayName("All files of current directory are listed with details to console")
    @Test
    public void listAllFilesWithDetails() throws IOException, InterruptedException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        Executable executable = displayInfo.listAllFilesWithDetails();
        executable.execute();

        File currentDir = new File(System.getProperty("user.dir"));
        File[] files = currentDir.listFiles();
        List<String> expectedOutput = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                String formattedTimestamp = formatTimestamp(file.lastModified());
                long size = file.isDirectory() ? displayInfo.getDirSize(file.toPath()) : file.length();
                String line = file.getName() + " " + file.isDirectory() + " " + size + " " + formattedTimestamp;
                expectedOutput.add(line);
            }
        } else {
            expectedOutput.add("No files found in the current directory.");
        }

        String actualOutput = outputStreamCaptor.toString().trim();
        List<String> actualLines = Arrays.stream(actualOutput.split(System.lineSeparator()))
                .collect(Collectors.toList());

        Assertions.assertEquals(actualLines, expectedOutput);


    }

    @DisplayName("File details are printed out")
    @Test
    public void printFileDetailsTest(@TempDir Path path) throws IOException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        String executable = displayInfo.printFileDetails(path);
        List<String> expectedAttributes = Arrays.asList("lastAccessTime",
                "lastModifiedTime",
                "size",
                "creationTime",
                "isSymbolicLink",
                "isRegularFile",
                "fileKey",
                "isOther",
                "isDirectory");
        for (String attribute : expectedAttributes) {
            assertTrue(executable.contains(attribute), "Expected attribute '" +
                    attribute + "' not found in output");
        }
    }

    @DisplayName("File with correct path is found")
    @Test
    public void findCorrectFileTest() throws IOException {
        String fileNameToFind = ".idea";
        Boolean result = displayInfo.findFiles(fileNameToFind);
        assertTrue(result, "Nothing found");
    }
}
