package main.origo.core;

public class NodeLoadException extends Exception {

    public final String nodeId;

    public NodeLoadException(String nodeId, String message) {
        super(message);
        this.nodeId = nodeId;
    }

    public NodeLoadException(String nodeId, String message, Throwable cause) {
        super(message, cause);
        this.nodeId = nodeId;
    }
}
