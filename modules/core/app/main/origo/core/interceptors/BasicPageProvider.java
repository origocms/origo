package main.origo.core.interceptors;


import main.origo.core.Node;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.UIElement;
import models.origo.core.BasicPage;
import models.origo.core.Content;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides and populates pages of type BasicPage.
 */
@Interceptor
public class BasicPageProvider {

    @Provides(type = "node", with = "models.origo.core.BasicPage")
    public static BasicPage loadPage(Provides.Context context) throws NodeNotFoundException {

        BasicPage page = BasicPage.findWithNodeIdAndSpecificVersion(context.rootNode.nodeId, context.rootNode.version);
        if (page == null) {
            throw new NodeNotFoundException(context.node.getNodeId());
        }
        page.rootNode = context.rootNode;

        return page;
    }

    @OnLoad(type = "node", with = "models.origo.core.BasicPage")
    public static void loadContent(OnLoad.Context context) {
        context.node.addUIElement(loadContent(((BasicPage) context.node).leadReferenceId));
        context.node.addUIElement(loadContent(((BasicPage) context.node).bodyReferenceId));
    }

    public static UIElement loadContent(String referenceId) {
        if (!StringUtils.isBlank(referenceId)) {
            Content content = Content.findWithIdentifier(referenceId);
            if (content != null) {
                return new UIElement(content.identifier, UIElement.PARAGRAPH, content.value);
            }
        }
        //TODO: Handle this somehow, in dev/admin maybe show a UIElement with a warning message and in prod swallow error?
        return null;
    }

}
