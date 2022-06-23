package ru.liga.utils;

import ru.liga.model.Currency;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class Forecasting {

    final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    final static SimpleDateFormat PRINT_FORMATTER = new SimpleDateFormat("E dd.MM.yyyy");

    /**
     * Прогнозирование курса валюты на следующий день от указанной
     *
     * @param rateHistory Курсы валют за определенный период
     * @param startDate Дата с которой ведётся отсчёт
     */
    public static void forecastingTomorrowRate(Map<String, Currency> rateHistory, Calendar startDate) {
        Calendar localStartDate = new GregorianCalendar();
        localStartDate.setTime(startDate.getTime());
        Calendar tomorrowDate = new GregorianCalendar();
        tomorrowDate.setTime(startDate.getTime());
        tomorrowDate.add(Calendar.DATE, 1);

        double forecastingRate = 0D;
        if (rateHistory.get(FORMATTER.format(tomorrowDate.getTime())) == null) {
            for (int i = 0; i < 7; ) {
                Currency currency = rateHistory.get(FORMATTER.format(localStartDate.getTime()));
                if (currency == null) {
                    localStartDate.add(Calendar.DATE, -1);
                    continue;
                }
                forecastingRate += currency.getRate() / currency.getNominal();
                localStartDate.add(Calendar.DATE, -1);
                i++;
            }
            forecastingRate /= 7;

            Currency tomorrowCurrency = new Currency(1, forecastingRate);

            rateHistory.put(FORMATTER.format(tomorrowDate.getTime()), tomorrowCurrency);
        }

        System.out.printf("%s - %.2f%n",
                PRINT_FORMATTER.format(tomorrowDate.getTime()),
                rateHistory.get(FORMATTER.format(tomorrowDate.getTime())).getRate());
    }

    /**
     * Прогнозирование курса валюты на неделю от текущей даты
     *
     * @param rateHistory Курсы валют за определенный период
     * @param startDate Дата с которой ведётся отсчёт
     */
    public static void forecastingWeekRate(Map<String, Currency> rateHistory, Calendar startDate) {
        Calendar weekDate = new GregorianCalendar();
        weekDate.setTime(startDate.getTime());

        for (int i = 0; i < 7; i++) {
            forecastingTomorrowRate(rateHistory, weekDate);
            weekDate.add(Calendar.DATE, 1);
        }
    }
}
