package ru.liga;

import ru.liga.controller.ConsoleController;
import ru.liga.controller.Controller;
import ru.liga.repository.CurrencyRepository;
import ru.liga.service.AvgForecasting;
import ru.liga.service.Forecasting;

import java.util.Scanner;

public class ForecastingCurrencyRates {

    public static void main(String[] args) {
        CurrencyRepository currencyRepository = CurrencyRepository.getInstance();
        Forecasting forecasting = new AvgForecasting(currencyRepository);
        Scanner scanner = new Scanner(System.in);
        Controller consoleController = new ConsoleController(forecasting, scanner);
        consoleController.getForecastingCurrencyRates();
    }
}