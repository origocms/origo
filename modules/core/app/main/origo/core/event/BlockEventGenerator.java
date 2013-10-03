package main.origo.core.event;

import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.ui.Element;
import models.origo.core.Block;

import java.util.Collections;

public class BlockEventGenerator {

    /*
     * Convenience methods for hooks with SEGMENT type
     */
    public static Element triggerBlockProvider(Node node, String withType, Block block) throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.CONTENT, withType, block, Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeBlockLoaded(Node node, String nodeType, String identifier) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.CONTENT, nodeType, Collections.<String, Object>singletonMap("identifier", identifier));
    }

    public static void triggerAfterBlockLoaded(Node node, String withType, Block block, Element element) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.CONTENT, withType, element, Collections.<String, Object>singletonMap("block", block));
    }

}
