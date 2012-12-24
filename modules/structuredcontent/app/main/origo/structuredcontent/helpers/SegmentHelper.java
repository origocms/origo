package main.origo.structuredcontent.helpers;

import main.origo.core.Node;
import main.origo.core.helpers.OnLoadHelper;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.ui.Element;
import models.origo.structuredcontent.Segment;

import java.util.Collections;

public class SegmentHelper {

    public static final String TYPE_SEGMENT = "segment";

    /*
     * Convenience methods for hooks with SEGMENT type
     */
    public static Element triggerSegmentProvider(String withType, Node node, Segment segment) {
        return ProvidesHelper.triggerInterceptor(TYPE_SEGMENT, withType, node, Collections.<String, Object>singletonMap("segment", segment));
    }

    public static void triggerBeforeSegmentLoaded(String nodeType, Node node, Segment segment) {
        OnLoadHelper.triggerBeforeInterceptor(TYPE_SEGMENT, nodeType, node, Collections.<String, Object>singletonMap("segment", segment));
    }

    public static void triggerAfterSegmentLoaded(String withType, Node node, Segment segment, Element element) {
        OnLoadHelper.triggerAfterInterceptor(TYPE_SEGMENT, withType, node, Collections.<String, Object>singletonMap("segment", segment), element);
    }

}
