package main.origo.core.helpers;


import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Types;
import main.origo.core.event.NodeContext;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

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
        // We'll set the root node for now, hopefully it will be overridden during load
        NodeContext.current().node = rootNode;

        return load(rootNode);
    }

    public static Node load(RootNode rootNode) throws NodeLoadException {
        boolean hasType = !StringUtils.isBlank(rootNode.nodeType) && !rootNode.nodeType.equals(RootNode.class.getName());
        if (hasType) {
            OnLoadEventGenerator.triggerBeforeInterceptor(Types.NODE, rootNode.nodeType, rootNode, Collections.<String, Object>emptyMap());
        }

        Node node = rootNode;
        if (hasType) {
            node = ProvidesEventGenerator.triggerInterceptor(Types.NODE, rootNode.nodeType, rootNode);
            if (node != null) {
                if (node.hasElements()) {
                    Logger.debug("The provider for [" + rootNode.nodeType + "] adds elements to during the create. Providers should only create the base type, and then to add elements use OnLoad instead. If the created Node doesn't use the same rootNode, elements will be lost.");
                }
                // We found a new type to override the root node with
                NodeContext.current().node = node;
            }
        }

        if (hasType) {
            OnLoadEventGenerator.triggerAfterInterceptor(Types.NODE, rootNode.nodeType, node, Collections.<String, Object>emptyMap());
        }

        return node;
    }

}
