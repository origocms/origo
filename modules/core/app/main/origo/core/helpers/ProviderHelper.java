package main.origo.core.helpers;

import main.origo.core.event.NodeContext;

public class ProviderHelper {

    public static boolean isProvider(String withType, Class clazz) {
        return NodeContext.current().attributes.get(withType).equals(clazz);
    }

}
