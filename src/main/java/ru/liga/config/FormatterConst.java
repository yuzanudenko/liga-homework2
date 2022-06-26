package ru.liga.config;

import java.time.format.DateTimeFormatter;

public class FormatterConst {
    //TODO: константы в отдельный класс
    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public final static DateTimeFormatter PRINT_FORMATTER = DateTimeFormatter.ofPattern("E dd.MM.yyyy");
}
