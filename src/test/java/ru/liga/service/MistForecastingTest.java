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

public class MistForecastingTest {

    Forecasting forecasting;
    CurrencyRepository currencyRepository;

    @Before
    public void setup() {
        currencyRepository = CurrencyRepository.getInstance();
        forecasting = new MistForecasting(currencyRepository);
    }

    @Test
    public void testForecastingSeveralRates() {
        LocalDate date = LocalDate.now().plusDays(1L);
        List<BigDecimal> curList = currencyRepository.findLastSeveralFromDateByName("Евро", date, 30).stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());

        List<BigDecimal> resultWeek = forecasting.forecastingSeveralRates("EUR", "WEEK").stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());

        assertThat(resultWeek).hasSize(7);
        for(BigDecimal cur : resultWeek) {
            assertThat(cur).isIn(curList);
        }

        List<BigDecimal> resultMonth = forecasting.forecastingSeveralRates("EUR", "MONTH").stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());
        assertThat(resultMonth).hasSize(30);
        for(BigDecimal cur : resultMonth) {
            assertThat(cur).isIn(curList);
        }
    }

    @Test
    public void testForecastingSpecificDateRate() {
        LocalDate date = LocalDate.now().plusDays(1L);
        List<BigDecimal> usdList = currencyRepository.findLastSeveralFromDateByName("Доллар США", date, 30).stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());
        List<BigDecimal> eurList = currencyRepository.findLastSeveralFromDateByName("Евро", date, 30).stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());
        List<BigDecimal> tryList = currencyRepository.findLastSeveralFromDateByName("Турецкая лира", date, 30).stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());
        List<BigDecimal> amdList = currencyRepository.findLastSeveralFromDateByName("Армянский драм", date, 30).stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());
        List<BigDecimal> bgnList = currencyRepository.findLastSeveralFromDateByName("Болгарский лев", date, 30).stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());


        assertThat(forecasting.forecastingSpecificDateRate("USD", date).getRateDividedNominal()).isIn(usdList);
        assertThat(forecasting.forecastingSpecificDateRate("EUR", date).getRateDividedNominal()).isIn(eurList);
        assertThat(forecasting.forecastingSpecificDateRate("TRY", date).getRateDividedNominal()).isIn(tryList);
        assertThat(forecasting.forecastingSpecificDateRate("AMD", date).getRateDividedNominal()).isIn(amdList);
        assertThat(forecasting.forecastingSpecificDateRate("BGN", date).getRateDividedNominal()).isIn(bgnList);
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