package main.origo.structuredcontent.interceptors;

import main.origo.core.Node;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;
import main.origo.structuredcontent.helpers.SegmentHelper;
import models.origo.core.RootNode;
import models.origo.structuredcontent.Segment;
import models.origo.structuredcontent.StructuredPage;

import java.util.List;

@Interceptor
public class StructuredPageProvider {

    @Provides(type = Core.Type.NODE, with = "models.origo.structuredcontent.StructuredPage")
    public static Node loadPage(Provides.Context context) throws NodeNotFoundException {
        StructuredPage page = StructuredPage.findWithNodeIdAndSpecificVersion(context.node.getNodeId(), context.node.getVersion());
        if (page == null) {
            throw new NodeNotFoundException(context.node.getNodeId());
        }
        page.rootNode = (RootNode) context.node;

        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = "models.origo.structuredcontent.StructuredPage")
    public static void loadContent(OnLoad.Context context) {

        List<Segment> segmentModels = Segment.findWithNodeIdAndSpecificVersion(context.node.getNodeId(), context.node.getVersion());
        for (Segment segment : segmentModels) {
            SegmentHelper.triggerBeforeSegmentLoaded(context.node, segment.type, segment);
            Element element = SegmentHelper.triggerSegmentProvider(context.node, segment.type, segment);
            SegmentHelper.triggerAfterSegmentLoaded(context.node, segment.type, segment, element);
            context.node.addElement(element);
        }

    }

}
