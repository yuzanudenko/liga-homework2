package ru.liga;

import ru.liga.controller.ConsoleController;
import ru.liga.controller.Controller;

public class ForecastingCurrencyRates {

    //TODO: main переписать, он должен быть максимально чистым. тут не должно быть никакого парсинга/вызова команд
    public static void main(String[] args) {
        Controller consoleController = new ConsoleController();
        consoleController.getForecastingCurrencyRates();
    }
}