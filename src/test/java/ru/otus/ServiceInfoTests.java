package ru.otus;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.impl.ServiceInfoImpl;
import ru.otus.run.RunCommand;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ServiceInfoTests {


    @DisplayName("Exit from app")
    @Test
    public void exitTest() {
        ServiceInfoImpl serviceInfo = new ServiceInfoImpl();
        assertDoesNotThrow(serviceInfo::exit);
    }

    @DisplayName("List all commands")
    @Test
    public void listAllCommands() {
        String[] resultArray = new String[]{"cd", "ls", "touch", "mv", "cp", "cd..",
                "exit", "help", "find", "finfo", "rm", "pwd", "mkdir"};
        RunCommand runCommand = new RunCommand();
        Object[] availableCommands = runCommand.getChooseMethod().keySet().toArray();
        Assertions.assertArrayEquals(resultArray, availableCommands);

    }
}
