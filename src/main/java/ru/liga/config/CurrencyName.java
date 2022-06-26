package ru.liga.config;

import lombok.Getter;

@Getter
public enum CurrencyName {
    EUR("Евро"),
    USD("Доллар США"),
    TRY("Турецкая лира");

    private final String translate;

    CurrencyName(String translate) {
        this.translate = translate;
    }

    public static CurrencyName getByName(String name) {
        for (CurrencyName currencyName : CurrencyName.values()) {
           if (currencyName.name().equalsIgnoreCase(name)) {
               return currencyName;
           }
        }
        return null;
    }
}
