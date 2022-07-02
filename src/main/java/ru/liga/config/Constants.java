package ru.liga.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Constants {
    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public final static DateTimeFormatter PRINT_FORMATTER = DateTimeFormatter.ofPattern("E dd.MM.yyyy");
    public final static DateFormat GRAPH_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
}