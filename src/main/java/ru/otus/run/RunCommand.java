package ru.otus.run;

import ru.otus.impl.ChangeDataImpl;
import ru.otus.impl.CreateDataImpl;
import ru.otus.impl.DisplayInfoImpl;
import ru.otus.impl.ServiceInfoImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RunCommand {

    private final Map<String, Executable> chooseMethod;

    public Map<String, Executable> getChooseMethod() {
        return chooseMethod;
    }

    private final DisplayInfoImpl displayInfo = new DisplayInfoImpl();
    private final ServiceInfoImpl serviceInfo = new ServiceInfoImpl();
    private final CreateDataImpl createData = new CreateDataImpl();
    private final ChangeDataImpl changeData = new ChangeDataImpl();

    @FunctionalInterface
    public interface Executable {
        void execute(String... args) throws IOException, InterruptedException;
    }

    public RunCommand() {
        chooseMethod = new HashMap<>();
        chooseMethod.put("pwd", displayInfo.printWorkingDirectory());
        chooseMethod.put("ls", (String... input) -> {
            if (input.length > 0 && input[0].equals("-l"))
                displayInfo.listAllFilesWithDetails().execute();
            else
                displayInfo.listAllFiles().execute();
        });
        chooseMethod.put("exit", serviceInfo.exit());
        chooseMethod.put("help", serviceInfo.help());
        chooseMethod.put("finfo", (String... input) -> {
            try {
                if (!input[0].equals(""))
                    System.out.println(displayInfo.printFileDetails(Path.of(input[0])));
            } catch (Exception e) {
                System.out.println("Enter file name");
            }
        });
        chooseMethod.put("cd", (String... input) -> {
            changeData.changeCurrentDirectory(Path.of(input[0]));
        });
        chooseMethod.put("cd..", changeData.goToParentDirectory());
        chooseMethod.put("mkdir", (String... input) -> {
            createData.createDirectory(input[0]);
        });
        chooseMethod.put("touch", (String... input) -> {
            createData.createFile(input[0]);
        });
        chooseMethod.put("mv", (String... input) -> {
            createData.moveFiles(Path.of(input[0]), Path.of(input[1]));
        });
        chooseMethod.put("cp", (String... input) -> {
            createData.copyFiles(Path.of(input[0]), Path.of(input[1]));
        });
        chooseMethod.put("find", (String... input) -> {
            displayInfo.findFiles(input[0]);
        });
        chooseMethod.put("rm", (String... input) -> {
            changeData.deleteFiles(Path.of(input[0]));
        });
    }


    public void callMethod(String input) throws IOException, InterruptedException {
        String[] split = input.split(" ");
        Executable executable = chooseMethod.get(split[0]);
        if (executable != null) {
            String[] args = new String[split.length - 1];
            for (int i = 0; i < split.length - 1; i++)
                args[i] = split[i + 1];
            executable.execute(args);
        } else {
            System.out.println("Команда " + split[0] + " не найдена");
        }
    }
}

