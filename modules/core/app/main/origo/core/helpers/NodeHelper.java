package main.origo.core.helpers;


import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Date;

public class NodeHelper {

    public static Node load(String nodeId) throws NodeNotFoundException, NodeLoadException {
        //Load RootNode model
        RootNode rootNode = RootNode.findLatestPublishedVersionWithNodeId(nodeId, new Date());
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }

        return load(rootNode);
    }

    public static Node load(String nodeId, Integer version) throws NodeNotFoundException, NodeLoadException {
        //Load RootNode model
        RootNode rootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }

        return load(rootNode);
    }

    public static Node load(RootNode rootNode) throws NodeLoadException {
        // We'll set the root node for now, hopefully it will be overridden during load
        NodeContext.current().node = rootNode;

        boolean hasType = !StringUtils.isBlank(rootNode.nodeType) && !rootNode.nodeType.equals(RootNode.class.getName());
        if (hasType) {
            OnLoadEventGenerator.triggerBeforeInterceptor(rootNode, Core.Type.NODE, rootNode.nodeType, Collections.<String, Object>emptyMap());
        }

        Node node = rootNode;
        if (hasType) {
            node = ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NODE, rootNode.nodeType);
            if (node != null) {
                // We found a new type to override the root node with
                NodeContext.current().node = node;
            }
        }

        if (hasType) {
            OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, rootNode.nodeType, Collections.<String, Object>emptyMap());
        }

        return node;
    }

}
