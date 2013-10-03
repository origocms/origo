package main.origo.core.interceptors;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.ContentHelper;
import main.origo.core.ui.Element;
import models.origo.core.Block;
import models.origo.core.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Interceptor
public class BlockProvider {

    @Provides(type = Block.TYPE, with = Text.TYPE)
    public static Element createBlock(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        Block block = (Block) args.get("block");
        if (block != null && !StringUtils.isBlank(block.referenceId)) {
            Element element = ContentHelper.loadContent(node, block.type, block.referenceId);
            if (element != null) {
                return element;
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
