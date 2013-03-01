package main.origo.core.interceptors;


import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;
import models.origo.core.BasicPage;
import models.origo.core.Content;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides and populates pages of type BasicPage.
 */
@Interceptor
public class BasicPageProvider {

    public static final String TYPE = "models.origo.core.BasicPage";

    @Provides(type = "node", with = TYPE)
    public static BasicPage loadPage(Provides.Context context) throws NodeNotFoundException {

        BasicPage page = BasicPage.findWithNodeIdAndSpecificVersion(context.node.getNodeId(), context.node.getVersion());
        if (page == null) {
            throw new NodeNotFoundException(context.node.getNodeId());
        }
        page.rootNode = (RootNode) context.node;

        return page;
    }

    @OnLoad(type = "node", with = TYPE)
    public static void loadContent(OnLoad.Context context) {
        context.node.addElement(loadContent(((BasicPage) context.node).leadReferenceId));
        context.node.addElement(loadContent(((BasicPage) context.node).bodyReferenceId));
    }

    private static Element loadContent(String referenceId) {
        if (!StringUtils.isBlank(referenceId)) {
            Content content = Content.findWithIdentifier(referenceId);
            if (content != null) {
                return new Element.Paragraph().setId(content.identifier).setBody(content.value);
            }
        }
        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
