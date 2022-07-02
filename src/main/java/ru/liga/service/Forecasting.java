package ru.liga.service;

import ru.liga.enums.Period;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.exception.WrongCommandException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Forecasting {

    protected final CurrencyRepository currencyRepository;

    public Forecasting(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Прогнозирование курса валюты на неделю от указанной даты
     *
     * @param currencyName Название валюты
     * @param specificDate Дата с которой ведётся отсчёт
     * @return Список спрогнозированных курсов указанной валюты
     */
    public abstract Currency forecastingSpecificDateRate(String currencyName, LocalDate specificDate);

    /**
     * Прогнозирование курса валюты на неделю от указанной даты
     *
     * @param currencyName Название валюты
     * @param period       Дата с которой ведётся отсчёт
     * @return Список спрогнозированных курсов указанной валюты
     */
    public List<Currency> forecastingSeveralRates(String currencyName, String period) {
        int days = Period.getByName(period)
                .orElseThrow(() -> new WrongCommandException("Wrong period!"))
                .getDays();
        LocalDate startDate = LocalDate.now();
        List<Currency> result = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            startDate = startDate.plusDays(1L);
            result.add(forecastingSpecificDateRate(currencyName, startDate));
        }
        return result;
    }
}
