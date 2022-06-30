package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CurrencyName {
    EUR("Евро"),
    USD("Доллар США"),
    TRY("Турецкая лира");

    private final String translate;

    public static Optional<CurrencyName> getByName(String name) {
        return Stream.of(CurrencyName.values())
                .filter((curName) -> curName.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
