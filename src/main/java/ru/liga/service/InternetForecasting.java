package ru.liga.service;

import ru.liga.enums.CurrencyName;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;
import ru.liga.utils.LinearRegression;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.math.RoundingMode.HALF_DOWN;

public class InternetForecasting extends Forecasting {

    public InternetForecasting(CurrencyRepository currencyRepository) {
        super(currencyRepository);
    }

    @Override
    public Currency forecastingSpecificDateRate(String currencyName, LocalDate specificDate) {
        LocalDate localStartDate = specificDate;
        localStartDate = localStartDate.minusDays(1L);
        String curTranslateName = CurrencyName.getByName(currencyName)
                .orElseThrow(() -> new CurrencyNotFoundException("Wrong currency!"))
                .getTranslate();

        List<Currency> lastSeveralCurrency = currencyRepository.findLastSeveralFromDateByName(curTranslateName, localStartDate, 30);
        double[] x = lastSeveralCurrency.stream().mapToDouble((cur) -> cur.getDate().toEpochDay()).toArray();
        double[] y = lastSeveralCurrency.stream().mapToDouble((cur) -> cur.getRateDividedNominal().doubleValue()).toArray();
        LinearRegression linearRegression = new LinearRegression(x, y);

        BigDecimal forecastingRate = BigDecimal.valueOf(linearRegression.predict(specificDate.toEpochDay()));

        Currency currency = new Currency(1, forecastingRate.setScale(3, HALF_DOWN), specificDate, curTranslateName);
        currencyRepository.addNewCurrency(currency);

        return currency;
    }
}
