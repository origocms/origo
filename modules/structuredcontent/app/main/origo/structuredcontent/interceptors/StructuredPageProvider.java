package main.origo.structuredcontent.interceptors;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.SegmentHelper;
import main.origo.core.ui.Element;
import models.origo.core.RootNode;
import models.origo.structuredcontent.StructuredPage;

import java.util.Map;

@Interceptor
public class StructuredPageProvider {

    @Provides(type = Core.Type.NODE, with = StructuredPage.TYPE)
    public static Node loadPage(Node node, String withType, Map<String, Object> args) throws NodeNotFoundException {
        StructuredPage page = StructuredPage.findWithNodeIdAndSpecificVersion(node.nodeId(), node.version());
        if (page == null) {
            throw new NodeNotFoundException(node.nodeId());
        }
        page.rootNode = (RootNode) node;

        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = StructuredPage.TYPE)
    public static void loadContent(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {

        StructuredPage page = (StructuredPage) node;

        for (String segmentIdentifier : page.segments) {
            Element element = SegmentHelper.loadSegment(node, segmentIdentifier);
            node.addElement(element);
        }

    }

}
