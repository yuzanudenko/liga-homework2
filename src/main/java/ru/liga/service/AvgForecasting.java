package ru.liga.service;

import ru.liga.enums.CurrencyName;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.math.RoundingMode.HALF_DOWN;

public class AvgForecasting extends Forecasting {

    public AvgForecasting(CurrencyRepository currencyRepository) {
        super(currencyRepository);
    }

    @Override
    public Currency forecastingSpecificDateRate(String currencyName, LocalDate specificDate) {
        LocalDate localStartDate = specificDate;
        String curTranslateName = CurrencyName.getByName(currencyName)
                .orElseThrow(() -> new CurrencyNotFoundException("Wrong currency!"))
                .getTranslate();

        BigDecimal forecastingRate = BigDecimal.ZERO;

        List<Currency> lastSevenCurrency = currencyRepository.findLastSeveralFromDateByName(curTranslateName, localStartDate, 7);
        for (Currency lastCurrency : lastSevenCurrency) {
            forecastingRate = forecastingRate.add(lastCurrency.getRateDividedNominal());
            localStartDate = localStartDate.minusDays(1L);
        }

        forecastingRate = forecastingRate.divide(BigDecimal.valueOf(7), HALF_DOWN);

        Currency currency = new Currency(1, forecastingRate, specificDate, curTranslateName);
        currencyRepository.addNewCurrency(currency);

        return currency;
    }
}
