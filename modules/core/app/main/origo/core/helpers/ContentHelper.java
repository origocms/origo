package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import models.origo.core.Content;

import java.util.Map;

public class ContentHelper {

    public static Content loadContent(Node node, String identifier) throws ModuleException, NodeLoadException {
        Map<String, Object> args = Maps.newHashMap();
        args.put("identifier", identifier);
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NODE, Content.TYPE);
        Content content = ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NODE, Content.TYPE, args);
        if (content != null) {
            args.put("content", content.detach());
            OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, Content.TYPE, args);
        }
        return content;
    }

}
