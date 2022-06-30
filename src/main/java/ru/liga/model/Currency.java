package ru.liga.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.RoundingMode.HALF_DOWN;

/**
 * Валюта
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    private Integer nominal;

    private BigDecimal rate;

    private LocalDate date;

    private String name;

    public BigDecimal getRateDividedNominal() {
        return this.rate.divide(BigDecimal.valueOf(this.nominal), 3, HALF_DOWN);
    }
}
