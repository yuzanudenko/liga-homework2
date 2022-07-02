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

public class AvgForecastingTest {

    Forecasting forecasting;
    CurrencyRepository currencyRepository;

    @Before
    public void setup() {
        currencyRepository = CurrencyRepository.getInstance();
        forecasting = new AvgForecasting(currencyRepository);
    }

    @Test
    public void testForecastingSeveralRates() {
        List<Currency> result = forecasting.forecastingSeveralRates("EUR", "WEEK");
        assertThat(result).hasSize(7);
        assertThat(result.stream().map(Currency::getRate).collect(Collectors.toList())).containsExactly(
                new BigDecimal("61.270"),
                new BigDecimal("60.799"),
                new BigDecimal("60.350"),
                new BigDecimal("60.101"),
                new BigDecimal("59.978"),
                new BigDecimal("60.082"),
                new BigDecimal("60.220"));
    }

    @Test
    public void testForecastingSpecificDateRate() {
        LocalDate date = LocalDate.now().plusDays(1L);

        assertThat(forecasting.forecastingSpecificDateRate("USD", date).getRate()).isEqualTo(new BigDecimal("58.282"));
        assertThat(forecasting.forecastingSpecificDateRate("EUR", date).getRate()).isEqualTo(new BigDecimal("61.270"));
        assertThat(forecasting.forecastingSpecificDateRate("TRY", date).getRate()).isEqualTo(new BigDecimal("3.419"));
        assertThat(forecasting.forecastingSpecificDateRate("AMD", date).getRate()).isEqualTo(new BigDecimal("0.198"));
        assertThat(forecasting.forecastingSpecificDateRate("BGN", date).getRate()).isEqualTo(new BigDecimal("55.053"));
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