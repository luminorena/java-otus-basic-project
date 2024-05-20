package ru.otus;

import ru.otus.run.RunCommand;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        RunCommand runCommand = new RunCommand();
        System.out.println("Добро пожаловать в консольный файловый менеджер");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.nextLine();
            runCommand.callMethod(next);
        }
    }
}
