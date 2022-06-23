package ru.liga.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    /**
     * Поиск файла c расширение *.csv по части названия
     *
     * @param currencyName Название валюты на латинице
     * @return Путь до файла
     */
    public static Path findFilePath(String currencyName) {
        URL location = FileUtils.class.getProtectionDomain().getCodeSource().getLocation();
        Path defaultPath;
        try {
            defaultPath = Paths.get(location.toURI())
                    .resolve("../classes/currencyHistoryData")
                    .normalize();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            return Files
                    .find(defaultPath, Integer.MAX_VALUE,
                            (path, basicFileAttributes) -> path
                                    .toFile()
                                    .getName()
                                    .matches("(\\s|\\d|\\w)*" + currencyName + "(\\s|\\d|\\w)*\\.csv")
                    )
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
