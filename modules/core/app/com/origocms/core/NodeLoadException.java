package com.origocms.core;

public class NodeLoadException extends Exception {

    public final String nodeId;

    public NodeLoadException(String nodeId, String message) {
        super(message);
        this.nodeId = nodeId;
    }

}
