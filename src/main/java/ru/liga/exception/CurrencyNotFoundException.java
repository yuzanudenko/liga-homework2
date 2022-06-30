package ru.liga.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(String msg) {
        super(msg);
    }

    public CurrencyNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    public CurrencyNotFoundException(Throwable e) {
        super(e);
    }
}
