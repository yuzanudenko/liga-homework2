package ru.liga.service;

import org.junit.Before;
import org.junit.Test;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.exception.WrongCommandException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InternetForecastingTest {

    Forecasting forecasting;
    CurrencyRepository currencyRepository;

    @Before
    public void setup() {
        currencyRepository = CurrencyRepository.getInstance();
        forecasting = new InternetForecasting(currencyRepository);
    }

    @Test
    public void testForecastingSeveralRates() {
        List<Currency> result = forecasting.forecastingSeveralRates("EUR", "WEEK");
        assertThat(result).hasSize(7);
        assertThat(result.stream().map(Currency::getRateDividedNominal).collect(Collectors.toList())).containsExactly(
                new BigDecimal("53.537"),
                new BigDecimal("53.800"),
                new BigDecimal("53.904"),
                new BigDecimal("53.766"),
                new BigDecimal("53.738"),
                new BigDecimal("53.838"),
                new BigDecimal("53.746"));
    }

    @Test
    public void testForecastingSpecificDateRate() {
        LocalDate date = LocalDate.now().plusDays(1L);

        assertThat(forecasting.forecastingSpecificDateRate("USD", date).getRateDividedNominal()).isEqualTo(new BigDecimal("51.198"));
        assertThat(forecasting.forecastingSpecificDateRate("EUR", date).getRateDividedNominal()).isEqualTo(new BigDecimal("53.537"));
        assertThat(forecasting.forecastingSpecificDateRate("TRY", date).getRateDividedNominal()).isEqualTo(new BigDecimal("2.560"));
        assertThat(forecasting.forecastingSpecificDateRate("AMD", date).getRateDividedNominal()).isEqualTo(new BigDecimal("0.317"));
        assertThat(forecasting.forecastingSpecificDateRate("BGN", date).getRateDividedNominal()).isEqualTo(new BigDecimal("88.841"));
    }

    @Test
    public void testForecastingExceptions() {
        LocalDate date = LocalDate.now().plusDays(1L);
        String wrongCurrency = "CHF";   // Швейцарский франк
        String wrongPeriod = "YEAR";    // Год

        assertThatThrownBy(() -> forecasting.forecastingSpecificDateRate(wrongCurrency, date)).isExactlyInstanceOf(CurrencyNotFoundException.class);
        assertThatThrownBy(() -> forecasting.forecastingSeveralRates("EUR", wrongPeriod)).isExactlyInstanceOf(WrongCommandException.class);
    }
}