package ru.liga.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Валюта
 */

@Getter
@Setter
public class Currency {

    private Integer nominal;
    private Double rate;

    private LocalDate date;

    private String name;

    //TODO: порядок объявления в классе должен быть по степени важности (используй Ctrl+Alt+Shift+L для открытия настройки форматтера кода и поставь галочки Rearrange, Cleanup, Optimize imports):
    public Currency(Integer nominal, Double rate, LocalDate date, String name) {
        this.nominal = nominal;
        this.rate = rate;
        this.date = date;
        this.name = name;
    }

    public Currency() {
    }
}
