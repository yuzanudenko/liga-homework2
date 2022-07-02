package ru.liga.service;

import ru.liga.exception.WrongCommandException;
import ru.liga.repository.CurrencyRepository;

public class ForecastingFactory {
    public static Forecasting createForecastingAlgorithm(String algCode, CurrencyRepository currencyRepository) {
        switch (algCode) {
            case "AVG":
                return new AvgForecasting(currencyRepository);
            case "MOON":
                return new MoonForecasting(currencyRepository);
            case "MIST":
                return new MistForecasting(currencyRepository);
            case "INTERNET":
                return new InternetForecasting(currencyRepository);
            default:
                throw new WrongCommandException("Wrong algorithm!");
        }
    }
}
