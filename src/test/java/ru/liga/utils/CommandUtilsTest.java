package ru.liga.utils;

import org.junit.Before;
import org.junit.Test;
import ru.liga.exception.WrongCommandException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommandUtilsTest {

    CommandUtils commandUtils;

    @Before
    public void setup() {
        commandUtils = new CommandUtils();
    }

    @Test
    public void testProcessInputCommandCorrectFourInputs() {
        Map<String, String> result = commandUtils.processInputCommand("rate EUR -period month -alg mist -output graph");
        assertThat(result).hasSize(4);
        assertThat(result.get("RATE")).isEqualTo("EUR");
        assertThat(result.get("DATE")).isNull();
        assertThat(result.get("PERIOD")).isEqualTo("MONTH");
        assertThat(result.get("ALG")).isEqualTo("MIST");
        assertThat(result.get("OUTPUT")).isEqualTo("GRAPH");
    }

    @Test
    public void testProcessInputCommandCorrectThreeInputs() {
        Map<String, String> result = commandUtils.processInputCommand("rate USD -date tomorrow -alg avg");
        assertThat(result).hasSize(4);
        assertThat(result.get("RATE")).isEqualTo("USD");
        assertThat(result.get("DATE")).isEqualTo("TOMORROW");
        assertThat(result.get("PERIOD")).isNull();
        assertThat(result.get("ALG")).isEqualTo("AVG");
        assertThat(result.get("OUTPUT")).isEqualTo("LIST");
    }

    @Test
    public void testProcessInputCommandCorrectFourInputsMoreSpaces() {
        Map<String, String> result = commandUtils.processInputCommand("rate     BGN   - period   month    - alg  moon    -  output   list");
        assertThat(result).hasSize(4);
        assertThat(result.get("RATE")).isEqualTo("BGN");
        assertThat(result.get("DATE")).isNull();
        assertThat(result.get("PERIOD")).isEqualTo("MONTH");
        assertThat(result.get("ALG")).isEqualTo("MOON");
        assertThat(result.get("OUTPUT")).isEqualTo("LIST");
    }

    @Test
    public void testProcessInputCommandCorrectThreeInputsMoreSpaces() {
        Map<String, String> result = commandUtils.processInputCommand("rate    TRY    -      date   14.07.2022 -  alg    internet");
        assertThat(result).hasSize(4);
        assertThat(result.get("RATE")).isEqualTo("TRY");
        assertThat(result.get("DATE")).isEqualTo("14.07.2022");
        assertThat(result.get("PERIOD")).isNull();
        assertThat(result.get("ALG")).isEqualTo("INTERNET");
        assertThat(result.get("OUTPUT")).isEqualTo("LIST");
    }

    @Test
    public void testProcessInputCommandWrongCommonCommandExceptions() {
        assertThatThrownBy(() -> commandUtils.processInputCommand("qwerty"))
                .isExactlyInstanceOf(WrongCommandException.class)
                .hasMessage("Wrong common command!");
    }

    @Test
    public void testProcessInputCommandEmptyRateExceptions() {
        assertThatThrownBy(() -> commandUtils.processInputCommand("rate -date tomorrow -alg avg -output list"))
                .isExactlyInstanceOf(WrongCommandException.class)
                .hasMessage("Empty rate!");
    }

    @Test
    public void testProcessInputCommandEmptyDatePeriodExceptions() {
        assertThatThrownBy(() -> commandUtils.processInputCommand("rate USD -alg avg -output list"))
                .isExactlyInstanceOf(WrongCommandException.class)
                .hasMessage("Empty date/period!");
    }

    @Test
    public void testProcessInputCommandEmptyAlgorithmExceptions() {
        assertThatThrownBy(() -> commandUtils.processInputCommand("rate EUR -date tomorrow -output graph"))
                .isExactlyInstanceOf(WrongCommandException.class)
                .hasMessage("Empty algorithm!");
    }
}