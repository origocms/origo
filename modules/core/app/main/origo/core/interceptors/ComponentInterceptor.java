package main.origo.core.interceptors;

import main.origo.core.Node;
import main.origo.core.actions.Component;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import models.origo.core.Content;

import java.util.Map;

@Interceptor
public class ComponentInterceptor {

    @OnLoad(with = Content.TYPE)
    public static void onLoadContent(Node node, String withType, Map<String, Object> args) {

        Content content = (Content) args.get("content");

        if (content != null && content.value.contains(Component.COMPONENT_MARKER)) {
            Component component = Component.getWrappedComponent();
            if (component != null) {
                content.value = content.value.replace(Component.COMPONENT_MARKER, component.body);
            }
        }

    }

}
