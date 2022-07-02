package ru.liga.utils;

import ru.liga.exception.WrongCommandException;

import java.util.HashMap;
import java.util.Map;

public class CommandUtils {
    public Map<String, String> processInputCommand(String commandLine) {
        String[] commands = commandLine.toUpperCase().trim().split("-");
        Map<String, String> commandMap = new HashMap<>();
        for (String command : commands) {
            String[] ddd = command.split("\\s+");
            commandMap.put(ddd[0], ddd[1]);
        }

        if ((commandMap.get("RATE") == null || commandMap.get("RATE").isEmpty()) ||
                commandMap.size() < 3 ||
                commandMap.size() > 4) {
            throw new WrongCommandException("Wrong common command!");
        }

        if ((commandMap.get("DATE") == null || commandMap.get("DATE").isEmpty()) &&
                (commandMap.get("PERIOD") == null || commandMap.get("PERIOD").isEmpty())) {
            throw new WrongCommandException("Empty date/period!");
        }

        if (commandMap.get("ALG") == null || commandMap.get("ALG").isEmpty()) {
            throw new WrongCommandException("Empty algorithm!");
        }

        if (commandMap.get("OUTPUT") == null || commandMap.get("OUTPUT").isEmpty()) {
            commandMap.put("OUTPUT", "LIST");
        }

        return commandMap;
    }
}
