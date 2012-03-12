package origo.core.listeners;

import models.origo.core.BasicPage;
import models.origo.core.RootNode;
import origo.core.annotations.Listener;
import origo.core.annotations.Provides;

@Listener
public class BasicPageProvider {

    @Provides(type = "node", with = "models.origo.core.BasicPage")
    public static BasicPage provide(String type, RootNode rootNode) {
        BasicPage page = BasicPage.findWithNodeIdAndSpecificVersion(rootNode.nodeId, rootNode.version);
        /*
        if (page == null) {
            throw new PageNotFoundException(rootNode.nodeId);
        }
        */
        page.publish = rootNode.publish;
        page.unPublish = rootNode.unPublish;
        page.themeVariant = rootNode.themeVariant;

        return page;
    }


}
