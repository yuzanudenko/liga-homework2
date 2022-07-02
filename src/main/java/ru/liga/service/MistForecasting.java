package ru.liga.service;

import ru.liga.enums.CurrencyName;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.Random;

public class MistForecasting extends Forecasting {

    public MistForecasting(CurrencyRepository currencyRepository) {
        super(currencyRepository);
    }

    @Override
    public Currency forecastingSpecificDateRate(String currencyName, LocalDate specificDate) {
        String curTranslateName = CurrencyName.getByName(currencyName)
                .orElseThrow(() -> new CurrencyNotFoundException("Wrong currency!"))
                .getTranslate();

        Currency mistCurrency = currencyRepository.findLastSeveralFromDateByName(curTranslateName, specificDate, 30)
                .get(new Random().nextInt(30));
        Currency currency = new Currency(1, mistCurrency.getRateDividedNominal(), specificDate, curTranslateName);
        currencyRepository.addNewCurrency(currency);

        return currency;
    }
}
