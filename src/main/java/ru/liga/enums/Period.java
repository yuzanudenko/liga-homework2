package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Period {
    WEEK(7),
    MONTH(30);

    private final int days;

    public static Optional<Period> getByName(String name) {
        return Stream.of(Period.values())
                .filter((curName) -> curName.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
