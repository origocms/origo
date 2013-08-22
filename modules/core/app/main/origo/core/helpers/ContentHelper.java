package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Content;

import java.util.Map;

public class ContentHelper {

    public static Element loadContent(Node node, String identifier) throws ModuleException, NodeLoadException {
        Map<String, Object> args = Maps.newHashMap();
        args.put("identifier", identifier);
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NODE, Content.TYPE);
        Element element = ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NODE, Content.TYPE, args);
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, Content.TYPE, element);
        return element;
    }

}
