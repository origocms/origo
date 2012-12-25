package main.origo.core.helpers;


import main.origo.core.Node;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Types;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Date;

public class NodeHelper {

    public static Node load(String nodeId) throws NodeNotFoundException {
        //Load RootNode model
        RootNode rootNode = RootNode.findLatestPublishedVersionWithNodeId(nodeId, new Date());
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }

        return load(rootNode);
    }

    public static Node load(String nodeId, Integer version) throws NodeNotFoundException {
        //Load RootNode model
        RootNode rootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }

        return load(rootNode);
    }

    public static Node load(RootNode rootNode) {
        boolean hasType = !StringUtils.isBlank(rootNode.nodeType) && !rootNode.nodeType.equals(RootNode.class.getName());
        if (hasType) {
            OnLoadEventGenerator.triggerBeforeInterceptor(Types.NODE, rootNode.nodeType, rootNode, Collections.<String, Object>emptyMap());
        }

        Node node = rootNode;
        if (hasType) {
            node = ProvidesEventGenerator.triggerInterceptor(Types.NODE, rootNode.nodeType, rootNode);
        }

        if (hasType) {
            OnLoadEventGenerator.triggerAfterInterceptor(Types.NODE, rootNode.nodeType, node, Collections.<String, Object>emptyMap());
        }

        return node;
    }

}
