package main.origo.core.interceptors;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.actions.Component;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;

import java.util.Map;

@Interceptor
public class ComponentInterceptor {

    @Provides(type = Core.Type.CONTENT, with = Component.TYPE)
    public static Element createSegment(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {

        Component component = Component.getWrappedComponent();
        if (component != null) {
            return new Element.Raw().setBody(component.body);
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
