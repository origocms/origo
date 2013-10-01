package main.origo.core.event;

import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.ui.Element;
import models.origo.core.Segment;

import java.util.Collections;

public class SegmentEventGenerator {

    /*
     * Convenience methods for hooks with SEGMENT type
     */
    public static Element triggerSegmentProvider(Node node, String withType, Segment segment) throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.CONTENT, withType, segment, Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeSegmentLoaded(Node node, String nodeType, String identifier) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.CONTENT, nodeType, Collections.<String, Object>singletonMap("identifier", identifier));
    }

    public static void triggerAfterSegmentLoaded(Node node, String withType, Segment segment, Element element) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.CONTENT, withType, element, Collections.<String, Object>singletonMap("segment", segment));
    }

}
