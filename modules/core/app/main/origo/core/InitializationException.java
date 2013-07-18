package main.origo.core;

public class InitializationException extends RuntimeException {

    public InitializationException() {
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String s, Throwable e) {
        super(s, e);
    }
}
