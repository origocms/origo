package main.origo.core;

public class NodeNotFoundException extends Exception {

    public final String nodeId;

    public NodeNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

}
