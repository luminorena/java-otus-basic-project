package ru.otus.impl;

import ru.otus.commands.ServiceInfo;
import ru.otus.run.RunCommand;
import ru.otus.run.RunCommand.Executable;

public class ServiceInfoImpl implements ServiceInfo {

    @Override
    public Executable exit() {
        return args -> {
            System.exit(-1);
        };
    }

    @Override
    public Executable help() {
        return args -> {
            RunCommand runCommand = new RunCommand();
            System.out.println(runCommand.getChooseMethod().keySet());
        };
    }
}
