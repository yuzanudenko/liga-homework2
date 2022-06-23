package ru.liga.model;

/**
 * Валюта
 */

public class Currency {

    public Currency(Integer nominal, Double rate) {
        this.nominal = nominal;
        this.rate = rate;
    }

    public Currency() {}

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    private Integer nominal;
    private Double rate;
}
