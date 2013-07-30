package main.origo.core.event;

import com.google.common.collect.Maps;
import main.origo.core.Node;

import java.util.Map;

public class NodeContext {

    private static ThreadLocal<NodeContext> current = new ThreadLocal<>();

    public Map<String, Object> attributes;
    public Node node;

    public NodeContext() {
        this.attributes = Maps.newHashMap();
    }

    public static NodeContext current() {
        return current.get();
    }

    public static void set() {
        if (current() == null) {
            current.set(new NodeContext());
        }
    }

    public static void clear() {
        if (current() != null) {
            current().attributes.clear();
            current.remove();
        }
    }

}
