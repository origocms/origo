package main.origo.core.actions;

import main.origo.core.event.NodeContext;

import java.util.Map;

public class Component {

    public static final String PATH_ALIAS = "PATH_ALIAS";
    public static final String COMPONENT_MARKER = "<!-- COMPONENT -->";

    public Map<String, String> headers;
    public String body;

    private Component() {}
    Component(Map<String, String> headers, String body) {
        this.headers = headers;
        this.body = body;
    }

    public static Component getWrappedComponent() {
        return (Component)NodeContext.current().attributes.get("component");
    }

    public static Component setWrappedComponent(Component component) {
        NodeContext.current().attributes.put("component", component);
        return component;
    }
}
