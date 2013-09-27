package main.origo.core.interceptors;

import main.origo.core.Node;
import main.origo.core.actions.Component;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.ui.Element;
import models.origo.core.Text;

import java.util.Map;

@Interceptor
public class ComponentInterceptor {

    @OnLoad(type = Core.Type.CONTENT, with = Text.TYPE)
    public static void onLoadContent(Node node, String withType, Element element, Map<String, Object> args) {

        if (element != null && element.getBody().body().contains(Component.COMPONENT_MARKER)) {
            Component component = Component.getWrappedComponent();
            if (component != null) {
                int idx = element.getBody().buffer().indexOf(Component.COMPONENT_MARKER);
                element.getBody().buffer().replace(idx, idx+Component.COMPONENT_MARKER.length(), component.body);
            }
        }

    }

}
