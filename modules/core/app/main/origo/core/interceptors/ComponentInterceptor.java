package main.origo.core.interceptors;

import main.origo.core.Node;
import main.origo.core.actions.Component;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import models.origo.core.Text;

import java.util.Map;

@Interceptor
public class ComponentInterceptor {

    @OnLoad(with = Text.TYPE)
    public static void onLoadContent(Node node, String withType, Text text, Map<String, Object> args) {

        if (text != null && text.value.contains(Component.COMPONENT_MARKER)) {
            Component component = Component.getWrappedComponent();
            if (component != null) {
                text.value = text.value.replace(Component.COMPONENT_MARKER, component.body);
            }
        }

    }

}
