package ru.otus.commands;

import ru.otus.run.RunCommand.Executable;


public interface ServiceInfo {
    Executable exit();

    Executable help();

}
