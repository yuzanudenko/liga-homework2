package ru.liga.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {    //TODO: убрать статику - это непотокобезопасно. Если была цель иметь только один экземпляр этого класса - нужно использовать Singleton паттерн.
    private static FileUtils instance;

    private FileUtils() {}

    public static synchronized FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }
        return instance;
    }

    /**
     * Поиск файла c расширение *.csv по части названия
     *
     * @param currencyName  Название валюты на латинице
     * @return Путь до файла
     */
    public Path findFilePath(String currencyName) {
        try {
            return Files
                    .find(getResourcesPath(), Integer.MAX_VALUE,
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

    /**
     * Получение пути до ресурсов
     *
     * @return Путь до ресурсов
     */
    private Path getResourcesPath() {
        URL location = FileUtils.class.getProtectionDomain().getCodeSource().getLocation();
        try {   //TODO: два try/catch'a - такого не должно быть, обычно это означает что у метода два назначения (см. Принципы SOLID, принцип единой ответственности, относится не только к классам, но и к методам)
            return Paths.get(location.toURI())
                    .resolve("../classes/currencyHistoryData")
                    .normalize();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);  //TODO: правильно, что перехватываешь все exception'ы, но было б неплохо, если бы они были кастомные, а не просто Runtime, Но требовать не буду
        }
    }
}
