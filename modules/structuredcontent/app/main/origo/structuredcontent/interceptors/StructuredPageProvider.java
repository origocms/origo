package main.origo.structuredcontent.interceptors;

import main.origo.core.Node;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Types;
import main.origo.core.ui.UIElement;
import main.origo.structuredcontent.helpers.SegmentHelper;
import models.origo.core.RootNode;
import models.origo.structuredcontent.Segment;
import models.origo.structuredcontent.StructuredPage;

import java.util.List;

public class StructuredPageProvider {

    @Provides(type = Types.NODE, with = "models.origo.structuredcontent.StructuredPage")
    public static Node loadPage(RootNode rootNode) throws NodeNotFoundException {
        StructuredPage page = StructuredPage.findWithNodeIdAndSpecificVersion(rootNode.nodeId, rootNode.version);
        if (page == null) {
            throw new NodeNotFoundException(rootNode.nodeId);
        }
        page.rootNode = rootNode;

        return page;
    }

    @OnLoad(type = Types.NODE, with = "models.origo.structuredcontent.StructuredPage")
    public static void loadContent(Node node) {

        List<Segment> segmentModels = Segment.findWithNodeIdAndSpecificVersion(node.getNodeId(), node.getVersion());
        for (Segment segment : segmentModels) {
            SegmentHelper.triggerBeforeSegmentLoaded(segment.type, node, segment);
            UIElement element = SegmentHelper.triggerSegmentProvider(segment.type, node, segment);
            SegmentHelper.triggerAfterSegmentLoaded(segment.type, node, segment, element);
            node.addUIElement(element);
        }

    }

}
