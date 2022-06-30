package ru.liga.controller;

import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.service.Forecasting;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static ru.liga.config.Constants.PRINT_FORMATTER;

public class ConsoleController implements Controller {

    private final Forecasting forecasting;
    private final Scanner scanner;

    public ConsoleController(Forecasting forecasting, Scanner scanner) {
        this.forecasting = forecasting;
        this.scanner = scanner;
    }

    public void getForecastingCurrencyRates() {
        while (true) {
            String commandLine = scanner.nextLine();

            String[] command = commandLine.toUpperCase().trim().split("\\s+");

            if ("EXIT".equals(command[0])) {
                break;
            } else if (!"RATE".equals(command[0]) || command.length != 3) {
                System.err.println("Wrong command!");
                continue;
            }

            try {
                LocalDate currentDate = LocalDate.now();
                List<Currency> result;
                switch (command[2]) {
                    case "TOMORROW":
                        result = forecasting.forecastingTomorrowRate(command[1], currentDate);
                        break;
                    case "WEEK":
                        result = forecasting.forecastingWeekRate(command[1], currentDate);
                        break;
                    default:
                        System.err.println("Wrong period!");
                        continue;
                }

                result.forEach((cur) -> System.out.printf("%s - %.2f%n", cur.getDate().format(PRINT_FORMATTER), cur.getRate()));
            } catch (CurrencyNotFoundException e) {
                System.err.println("Wrong currency!");
            }
        }

        scanner.close();
    }
}
