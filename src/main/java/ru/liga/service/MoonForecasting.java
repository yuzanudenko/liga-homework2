package ru.liga.service;

import ru.liga.enums.CurrencyName;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;

public class MoonForecasting extends Forecasting {

    public MoonForecasting(CurrencyRepository currencyRepository) {
        super(currencyRepository);
    }

    @Override
    public Currency forecastingSpecificDateRate(String currencyName, LocalDate specificDate) {
        LocalDate localStartDate = specificDate;
        String curTranslateName = CurrencyName.getByName(currencyName)
                .orElseThrow(() -> new CurrencyNotFoundException("Wrong currency!"))
                .getTranslate();

        Currency moonCurrency;
        localStartDate = localStartDate.minusYears(1L);
        while (true) {
            moonCurrency = currencyRepository.findOneByNameAndDate(curTranslateName, localStartDate);
            if (moonCurrency != null) {
                break;
            }
            localStartDate = localStartDate.minusDays(1L);
        }

        Currency currency = new Currency(1, moonCurrency.getRateDividedNominal(), specificDate, curTranslateName);
        currencyRepository.addNewCurrency(currency);

        return currency;
    }
}
