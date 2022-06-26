package ru.liga.service;

import ru.liga.model.Currency;

import java.time.LocalDate;
import java.util.List;

public interface Forecasting {

    List<Currency> forecastingTomorrowRate(String currencyName, LocalDate startDate);

    List<Currency> forecastingWeekRate(String currencyName, LocalDate startDate);
}
