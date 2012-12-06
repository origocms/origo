package com.origocms.core;

public class PageNotFoundException extends RuntimeException {

    public final String nodeId;

    public PageNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

}
