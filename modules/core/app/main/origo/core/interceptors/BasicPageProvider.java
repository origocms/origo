package main.origo.core.interceptors;


import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.ContentHelper;
import main.origo.core.ui.Element;
import models.origo.core.BasicPage;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides and populates pages of type BasicPage.
 */
@Interceptor
public class BasicPageProvider {

    public static final String TYPE = BasicPage.TYPE;

    @Provides(type = Core.Type.NODE, with = TYPE)
    public static BasicPage loadPage(Provides.Context context) throws NodeNotFoundException {

        BasicPage page = BasicPage.findWithNodeIdAndSpecificVersion(context.node.nodeId(), context.node.version());
        if (page == null) {
            throw new NodeNotFoundException(context.node.nodeId());
        }
        page.rootNode = (RootNode) context.node;

        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = TYPE)
    public static void loadContent(OnLoad.Context context) throws ModuleException, NodeLoadException {
        context.node.addElement(loadContent(context, ((BasicPage) context.node).leadReferenceId));
        context.node.addElement(loadContent(context, ((BasicPage) context.node).bodyReferenceId));
    }

    private static Element loadContent(OnLoad.Context context, String referenceId) throws NodeLoadException, ModuleException {
        if (!StringUtils.isBlank(referenceId)) {
            return ContentHelper.loadContent(context.node, referenceId);
        }
        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
