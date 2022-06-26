package ru.liga.controller;

import ru.liga.model.Currency;
import ru.liga.service.Forecasting;
import ru.liga.service.ForecastingLastSeven;

import java.time.LocalDate;
import java.util.*;

import static ru.liga.config.FormatterConst.PRINT_FORMATTER;

public class ConsoleController implements Controller {
    public void getForecastingCurrencyRates() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String commandLine = in.nextLine();
            String[] command = commandLine.toUpperCase().trim().split("\\s+");

            if ("EXIT".equals(command[0])) {
                break;
            } else if (!"RATE".equals(command[0]) || command.length != 3) {
                System.out.println("Wrong command!");
                continue;
            }

            LocalDate currentDate = LocalDate.now();
            Forecasting forecastingLastSeven = new ForecastingLastSeven();
            List<Currency> result = new ArrayList<>();
            switch (command[2]) {
                case "TOMORROW":
                    result = forecastingLastSeven.forecastingTomorrowRate(command[1], currentDate);
                    break;
                case "WEEK":
                    result = forecastingLastSeven.forecastingWeekRate(command[1], currentDate);
                    break;
                default:
                    System.out.println("Wrong period!");
            }

            result.forEach((cur) -> System.out.printf("%s - %.2f%n", cur.getDate().format(PRINT_FORMATTER), cur.getRate()));
        }

        in.close();
    }
}
