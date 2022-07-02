package ru.liga.controller;

import lombok.extern.slf4j.Slf4j;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.service.Forecasting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ru.liga.config.Constants.PRINT_FORMATTER;

@Slf4j
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
            log.debug("Start processing command - " + commandLine);

            String[] command = commandLine.toUpperCase().trim().split("\\s+");

            if ("EXIT".equals(command[0])) {
                break;
            } else if (!"RATE".equals(command[0]) || command.length != 3) {
                log.error(commandLine + " - Wrong command!");
                continue;
            }

            try {
                LocalDate currentDate = LocalDate.now();
                List<Currency> result = new ArrayList<>();
                if ("TOMORROW".equals(command[2])) {
                    currentDate = currentDate.plusDays(1L);
                    result.add(forecasting.forecastingSpecificDateRate(command[1], currentDate));
                } else {
                    result = forecasting.forecastingSeveralRates(command[1], command[2]);
                }

                result.forEach((cur) -> log.info(String.format("%s - %.2f", cur.getDate().format(PRINT_FORMATTER), cur.getRate())));
            } catch (CurrencyNotFoundException e) {
                log.error("Wrong currency!");
            }
        }

        scanner.close();
    }
}
