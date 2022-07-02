package ru.liga.exception;

public class WrongCommandException extends RuntimeException {

    public WrongCommandException(String msg) {
        super(msg);
    }

    public WrongCommandException(String msg, Throwable e) {
        super(msg, e);
    }

    public WrongCommandException(Throwable e) {
        super(e);
    }
}
