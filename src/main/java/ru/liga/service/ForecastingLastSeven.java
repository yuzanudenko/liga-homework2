package ru.liga.service;

import ru.liga.config.CurrencyName;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ForecastingLastSeven implements Forecasting {  //TODO: статика
    //TODO: методы класса нетестируемые, хотя содержат бизнесовую логику
    //TODO: методы класса завязаны на конкретную реализацию. Если изменится интерфейс вызова с консоли, напр., на рест - придется полностью переписывать код

    CurrencyRepository currencyRepository = CurrencyRepository.getInstance();

    /**
     * Прогнозирование курса валюты на следующий день от заданной даты
     *
     * @param currencyName  Название валюты
     * @param startDate     Дата с которой ведётся отсчёт
     * @return Список спрогнозированных курсов указанной валюты
     */
    //TODO: у этого метода слишком много ответственности - это не просто сервис прогноза, а сервис который форматирует, считает и выводит в консоль команду
    //TODO: а что если изменится алгоритм или добавится новый? Нельзя влезать в реализацию текущего метода, если добавляется новый алгоритм
    public List<Currency> forecastingTomorrowRate(String currencyName, LocalDate startDate) {
        LocalDate localStartDate = startDate;  //TODO: лучше использовать новый API - java.util.time.LocalDate, с ним проще работать
        LocalDate tomorrowDate = startDate;
        tomorrowDate = tomorrowDate.plusDays(1L);
        String curTranslateName = CurrencyName.getByName(currencyName).getTranslate();
        List<Currency> result = new ArrayList<>();
        Currency currency = currencyRepository.findOneByNameAndDate(curTranslateName, tomorrowDate);

        if (currency == null) {
            double forecastingRate = 0D;

            List<Currency> lastSevenCurrency = currencyRepository.findLastSevenFromDateByName(curTranslateName, localStartDate);
            for (Currency lastCurrency : lastSevenCurrency) {
                forecastingRate += lastCurrency.getRate() / lastCurrency.getNominal();
                localStartDate = localStartDate.minusDays(1L);
            }

            forecastingRate /= 7;

            currency = new Currency(1, forecastingRate, tomorrowDate, curTranslateName);
            currencyRepository.addNewCurrency(currency);
        }
        result.add(currency);

        return result;
    }

    /**
     * Прогнозирование курса валюты на неделю от указанной даты
     *
     * @param currencyName  Название валюты
     * @param startDate     Дата с которой ведётся отсчёт
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
