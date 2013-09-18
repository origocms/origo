package main.origo.core.interceptors;


import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.ContentHelper;
import main.origo.core.ui.Element;
import models.origo.core.BasicPage;
import models.origo.core.Content;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Provides and populates pages of type BasicPage.
 */
@Interceptor
public class BasicPageProvider {

    public static final String TYPE = BasicPage.TYPE;

    @Provides(type = Core.Type.NODE, with = TYPE)
    public static BasicPage loadPage(Node node, String withType, Map<String, Object> args) throws NodeNotFoundException {

        BasicPage page = BasicPage.findWithNodeIdAndSpecificVersion(node.nodeId(), node.version());
        if (page == null) {
            throw new NodeNotFoundException(node.nodeId());
        }
        page.rootNode = (RootNode) node;

        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = TYPE)
    public static void loadContent(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        node.addElement(loadContent(node, ((BasicPage) node).leadReferenceId));
        node.addElement(loadContent(node, ((BasicPage) node).bodyReferenceId));
    }

    private static Element loadContent(Node node, String referenceId) throws NodeLoadException, ModuleException {
        if (!StringUtils.isBlank(referenceId)) {
            Content content = ContentHelper.loadContent(node, referenceId);
            if (content != null) {
                return new Element.Panel().setId(content.identifier).setBody(content.value);
            }
        }
        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
