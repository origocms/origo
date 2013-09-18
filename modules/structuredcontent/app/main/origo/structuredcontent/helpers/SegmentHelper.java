package main.origo.structuredcontent.helpers;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;
import models.origo.structuredcontent.Segment;

import java.util.Collections;

public class SegmentHelper {

    public static final String TYPE_SEGMENT = "segment";

    /*
     * Convenience methods for hooks with SEGMENT type
     */
    public static Element triggerSegmentProvider(Node node, String withType, Segment segment) throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(node, TYPE_SEGMENT, withType, Collections.<String, Object>singletonMap("segment", segment));
    }

    public static void triggerBeforeSegmentLoaded(Node node, String nodeType, Segment segment) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node,  TYPE_SEGMENT, nodeType, Collections.<String, Object>singletonMap("segment", segment));
    }

    public static void triggerAfterSegmentLoaded(Node node, String withType, Segment segment, Element element) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerAfterInterceptor(node, TYPE_SEGMENT, withType, element, Collections.<String, Object>singletonMap("segment", segment));
    }

}
