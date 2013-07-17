package main.origo.core;

public class ModuleException extends Exception {

    public final String name;
    public final Cause cause;

    public ModuleException(String name, Cause cause) {
        super();
        this.name = name;
        this.cause = cause;
    }

    public ModuleException(String name, Cause cause, String message) {
        super(message);
        this.name = name;
        this.cause = cause;
    }

    public ModuleException(String name, Cause cause, String message, Throwable throwable) {
        super(message, throwable);
        this.name = name;
        this.cause = cause;
    }

    public static enum Cause {
        NOT_INITIALIZED, NOT_INSTALLED, NOT_ENABLED
    }
}
