package ru.liga.utils;

import lombok.extern.slf4j.Slf4j;
import ru.liga.exception.WrongCommandException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommandUtils {
    public Map<String, String> processInputCommand(String commandLine) {
        String[] commands = commandLine.toUpperCase().trim().split("-");
        Map<String, String> commandMap = new HashMap<>();
        for (String command : commands) {
            String[] splitCommand = command.trim().split("\\s+");
            if (splitCommand.length == 2) {
                commandMap.put(splitCommand[0], splitCommand[1]);
            }
        }

        if (commandMap.size() < 3 || commandMap.size() > 4) {
            throw new WrongCommandException("Wrong common command!");
        }

        if (commandMap.get("RATE") == null || commandMap.get("RATE").isEmpty()) {
            throw new WrongCommandException("Empty rate!");
        }

        if ((commandMap.get("DATE") == null || commandMap.get("DATE").isEmpty()) &&
                (commandMap.get("PERIOD") == null || commandMap.get("PERIOD").isEmpty())) {
            throw new WrongCommandException("Empty date/period!");
        }

        if (commandMap.get("ALG") == null || commandMap.get("ALG").isEmpty()) {
            throw new WrongCommandException("Empty algorithm!");
        }

        if (commandMap.get("OUTPUT") == null || commandMap.get("OUTPUT").isEmpty()) {
            log.debug("Empty output. Set default value - list.");
            commandMap.put("OUTPUT", "LIST");
        }

        return commandMap;
    }
}
