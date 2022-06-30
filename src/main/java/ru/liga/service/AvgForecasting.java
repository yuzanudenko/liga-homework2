package ru.liga.service;

import ru.liga.enums.CurrencyName;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.math.RoundingMode.HALF_DOWN;

public class AvgForecasting implements Forecasting {

    private final CurrencyRepository currencyRepository;

    public AvgForecasting(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Прогнозирование курса валюты на следующий день от заданной даты
     *
     * @param currencyName Название валюты
     * @param startDate    Дата с которой ведётся отсчёт
     * @return Список спрогнозированных курсов указанной валюты
     */
    //TODO: а что если изменится алгоритм или добавится новый? Нельзя влезать в реализацию текущего метода, если добавляется новый алгоритм
    public List<Currency> forecastingTomorrowRate(String currencyName, LocalDate startDate) {
        LocalDate localStartDate = startDate;
        LocalDate tomorrowDate = startDate;
        tomorrowDate = tomorrowDate.plusDays(1L);
        String curTranslateName = CurrencyName.getByName(currencyName)
                .orElseThrow(CurrencyNotFoundException::new)
                .getTranslate();
        List<Currency> result = new ArrayList<>();
        Currency currency = currencyRepository.findOneByNameAndDate(curTranslateName, tomorrowDate);

        if (currency == null) {
            BigDecimal forecastingRate = BigDecimal.ZERO;

            List<Currency> lastSevenCurrency = currencyRepository.findLastSevenFromDateByName(curTranslateName, localStartDate);
            for (Currency lastCurrency : lastSevenCurrency) {
                forecastingRate = forecastingRate.add(lastCurrency.getRateDividedNominal());
                localStartDate = localStartDate.minusDays(1L);
            }

            forecastingRate = forecastingRate.divide(BigDecimal.valueOf(7), HALF_DOWN);

            currency = new Currency(1, forecastingRate, tomorrowDate, curTranslateName);
            currencyRepository.addNewCurrency(currency);
        }
        result.add(currency);

        return result;
    }

    /**
     * Прогнозирование курса валюты на неделю от указанной даты
     *
     * @param currencyName Название валюты
     * @param startDate    Дата с которой ведётся отсчёт
     * @return Список спрогнозированных курсов указанной валюты
     */
    public List<Currency> forecastingWeekRate(String currencyName, LocalDate startDate) {
        LocalDate weekDate = startDate;
        List<Currency> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            result.addAll(forecastingTomorrowRate(currencyName, weekDate));
            weekDate = weekDate.plusDays(1L);
        }
        return result;
    }
}
