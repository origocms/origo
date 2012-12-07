package com.origocms.core.helpers;


import com.origocms.core.Node;
import com.origocms.core.NodeNotFoundException;
import com.origocms.core.annotations.Types;
import com.origocms.core.models.RootNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class NodeHelper {

    public static Node load(String nodeId) {
        //Load RootNode model
        RootNode rootNode = RootNode.findLatestPublishedVersionWithNodeId(nodeId, new Date());
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }

        return load(rootNode);
    }

    public static Node load(String nodeId, long version) {
        //Load RootNode model
        RootNode rootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }

        return load(rootNode);
    }

    public static Node load(RootNode rootNode) {
        boolean hasType = !StringUtils.isBlank(rootNode.type) && !rootNode.type.equals(RootNode.class.getName());
        if (hasType) {
            triggerBeforeNodeLoaded(rootNode.type, rootNode);
        }

        Node node = rootNode;
        if (hasType) {
            node = triggerProvidesNodeListener(rootNode.type, rootNode);
        }

        if (hasType) {
            triggerAfterNodeLoaded(rootNode.type, node);
        }

        return node;
    }

    /*
     * Convenience methods for hooks with NODE type
     */
    public static Node triggerProvidesNodeListener(String withType, RootNode rootNode) {
        return ProvidesHelper.triggerListener(Types.NODE, withType, rootNode);
    }

    public static void triggerBeforeNodeLoaded(String withType, RootNode rootNode) {
        OnLoadHelper.triggerBeforeListener(Types.NODE, withType, rootNode);
    }

    public static void triggerAfterNodeLoaded(String withType, Node node) {
        OnLoadHelper.triggerAfterListener(Types.NODE, withType, node);
    }

}
