package main.origo.core.helpers;


import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.preview.PreviewEventGenerator;
import main.origo.core.preview.Ticket;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.Collections;

public class NodeHelper {

    public static Node load(String nodeId) throws NodeNotFoundException, NodeLoadException, ModuleException {

        RootNode rootNode = loadRootNode(nodeId, 0);

        return load(rootNode);
    }

    public static Node load(String nodeId, Integer version) throws NodeNotFoundException, NodeLoadException, ModuleException {

        RootNode rootNode = loadRootNode(nodeId, version);

        return load(rootNode);
    }

    public static RootNode loadRootNode(String nodeId, Integer version) throws ModuleException, NodeLoadException, NodeNotFoundException {
        RootNode rootNode;
        // Check for a preview ticket and load the corresponding rootnode
        Ticket ticket = PreviewEventGenerator.getValidTicket();
        if (ticket != null) {
            Logger.trace("Preview Ticket found, ignoring version and using preview date of: "+ticket.preview().toString());
            rootNode = RootNode.findPublishedVersionWithNodeIdAndDate(nodeId, ticket.preview().toDate());
        } else {
            //Load RootNode model
            if (version > 0) {
                rootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
            } else {
                rootNode = RootNode.findLatestPublishedVersionWithNodeId(nodeId);
            }
        }
        if (rootNode == null) {
            throw new NodeNotFoundException(nodeId);
        }
        return rootNode;
    }

    public static Node load(RootNode rootNode) throws NodeLoadException, ModuleException {
        return load(rootNode, Core.Type.NODE);
    }

    public static Node load(RootNode rootNode, String type) throws NodeLoadException, ModuleException {
        // We'll set the root node for now, hopefully it will be overridden during load
        NodeContext.current().node = rootNode;

        boolean hasType = !StringUtils.isBlank(rootNode.nodeType()) && !rootNode.nodeType().equals(RootNode.class.getName());
        if (hasType) {
            OnLoadEventGenerator.triggerBeforeInterceptor(rootNode, type, rootNode.nodeType(), Collections.<String, Object>emptyMap());
        }

        Node node = rootNode;
        if (hasType) {
            node = ProvidesEventGenerator.triggerInterceptor(node, type, rootNode.nodeType());
            if (node != null) {
                // We found a new type to override the root node with
                NodeContext.current().node = node;
            }
        }

        if (hasType) {
            OnLoadEventGenerator.triggerAfterInterceptor(node, type, rootNode.nodeType(), Collections.<String, Object>emptyMap());
        }

        return node;
    }

}
