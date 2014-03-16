package main.origo.core;

public class NoSuchProviderException extends RuntimeException {

    public final String type;
    public final String with;

    public NoSuchProviderException(String type, String with) {
        this.type = type;
        this.with = with;
    }

    public NoSuchProviderException(String s, String type, String with) {
        super(s);
        this.type = type;
        this.with = with;
    }

}
