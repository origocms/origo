package main.origo.structuredcontent.helpers;

import main.origo.core.Node;
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
    public static Element triggerSegmentProvider(String withType, Node node, Segment segment) {
        return ProvidesEventGenerator.triggerInterceptor(TYPE_SEGMENT, withType, node, Collections.<String, Object>singletonMap("segment", segment));
    }

    public static void triggerBeforeSegmentLoaded(String nodeType, Node node, Segment segment) {
        OnLoadEventGenerator.triggerBeforeInterceptor(TYPE_SEGMENT, nodeType, node, Collections.<String, Object>singletonMap("segment", segment));
    }

    public static void triggerAfterSegmentLoaded(String withType, Node node, Segment segment, Element element) {
        OnLoadEventGenerator.triggerAfterInterceptor(TYPE_SEGMENT, withType, node, Collections.<String, Object>singletonMap("segment", segment), element);
    }

}
