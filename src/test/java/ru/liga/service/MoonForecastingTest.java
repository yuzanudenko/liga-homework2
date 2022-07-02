package ru.liga.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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

public class MoonForecastingTest {

    Forecasting forecastingMock;
    CurrencyRepository currencyRepositoryMock;

    @Before
    public void setup() {
        currencyRepositoryMock = Mockito.spy(CurrencyRepository.getInstance());
        forecastingMock = new MoonForecasting(currencyRepositoryMock);
    }

    @Test
    public void testForecastingSeveralRates() {
        LocalDate date = LocalDate.now().minusYears(1L);

        Mockito.doReturn(new Currency(1, new BigDecimal("34.562"), date.plusDays(1L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(1L));
        Mockito.doReturn(new Currency(1, new BigDecimal("45.633"), date.plusDays(2L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(2L));
        Mockito.doReturn(new Currency(1, new BigDecimal("42.795"), date.plusDays(3L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(3L));
        Mockito.doReturn(new Currency(1, new BigDecimal("41.342"), date.plusDays(4L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(4L));
        Mockito.doReturn(new Currency(1, new BigDecimal("34.656"), date.plusDays(5L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(5L));
        Mockito.doReturn(new Currency(1, new BigDecimal("43.807"), date.plusDays(6L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(6L));
        Mockito.doReturn(new Currency(1, new BigDecimal("88.473"), date.plusDays(7L), "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", date.plusDays(7L));

        List<BigDecimal> resultWeek = forecastingMock.forecastingSeveralRates("EUR", "WEEK").stream()
                .map(Currency::getRateDividedNominal).collect(Collectors.toList());
        assertThat(resultWeek).hasSize(7);
        assertThat(resultWeek).containsExactly(
                new BigDecimal("34.562"),
                new BigDecimal("45.633"),
                new BigDecimal("42.795"),
                new BigDecimal("41.342"),
                new BigDecimal("34.656"),
                new BigDecimal("43.807"),
                new BigDecimal("88.473"));
    }

    @Test
    public void testForecastingSpecificDateRate() {
        LocalDate date = LocalDate.now();
        LocalDate mockDate = LocalDate.now().minusYears(1L);

        Mockito.doReturn(new Currency(1, new BigDecimal("73.766"), mockDate, "Доллар США"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Доллар США", mockDate);
        Mockito.doReturn(new Currency(1, new BigDecimal("86.845"), mockDate, "Евро"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Евро", mockDate);
        Mockito.doReturn(new Currency(1, new BigDecimal("8.622"), mockDate, "Турецкая лира"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Турецкая лира", mockDate);
        Mockito.doReturn(new Currency(1, new BigDecimal("0.151"), mockDate, "Армянский драм"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Армянский драм", mockDate);
        Mockito.doReturn(new Currency(1, new BigDecimal("44.427"), mockDate, "Болгарский лев"))
                .when(currencyRepositoryMock)
                .findOneByNameAndDate("Болгарский лев", mockDate);

        assertThat(forecastingMock.forecastingSpecificDateRate("USD", date).getRateDividedNominal()).isEqualTo(new BigDecimal("73.766"));
        assertThat(forecastingMock.forecastingSpecificDateRate("EUR", date).getRateDividedNominal()).isEqualTo(new BigDecimal("86.845"));
        assertThat(forecastingMock.forecastingSpecificDateRate("TRY", date).getRateDividedNominal()).isEqualTo(new BigDecimal("8.622"));
        assertThat(forecastingMock.forecastingSpecificDateRate("AMD", date).getRateDividedNominal()).isEqualTo(new BigDecimal("0.151"));
        assertThat(forecastingMock.forecastingSpecificDateRate("BGN", date).getRateDividedNominal()).isEqualTo(new BigDecimal("44.427"));
    }

    @Test
    public void testForecastingExceptions() {
        LocalDate date = LocalDate.now().plusDays(1L);
        String wrongCurrency = "CHF";   // Швейцарский франк
        String wrongPeriod = "YEAR";    // Год

        assertThatThrownBy(() -> forecastingMock.forecastingSpecificDateRate(wrongCurrency, date)).isExactlyInstanceOf(CurrencyNotFoundException.class);
        assertThatThrownBy(() -> forecastingMock.forecastingSeveralRates("EUR", wrongPeriod)).isExactlyInstanceOf(WrongCommandException.class);
    }
}