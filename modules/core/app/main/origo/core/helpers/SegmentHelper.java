package main.origo.core.helpers;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.event.SegmentEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Segment;

public class SegmentHelper {

    public static Element loadSegment(Node node, String identifier) throws ModuleException, NodeLoadException {
        Segment segment = Segment.findWithIdentifier(identifier);
        SegmentEventGenerator.triggerBeforeSegmentLoaded(node, segment.type, identifier);
        Element element = SegmentEventGenerator.triggerSegmentProvider(node, segment.type, segment);
        SegmentEventGenerator.triggerAfterSegmentLoaded(node, segment.type, segment, element);
        return element;
    }

}
