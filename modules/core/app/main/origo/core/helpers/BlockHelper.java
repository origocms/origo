package main.origo.core.helpers;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.event.BlockEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Block;

public class BlockHelper {

    public static Element loadBlock(Node node, String identifier) throws ModuleException, NodeLoadException {
        Block block = Block.findWithIdentifier(identifier);
        BlockEventGenerator.triggerBeforeBlockLoaded(node, block.type, identifier);
        Element element = BlockEventGenerator.triggerBlockProvider(node, block.type, block);
        BlockEventGenerator.triggerAfterBlockLoaded(node, block.type, block, element);
        return element;
    }

}
