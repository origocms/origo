package controllers.listeners;

import models.BasicPage;
import models.RootNode;
import controllers.annotations.Listener;
import controllers.annotations.Provides;

@Listener
public class BasicPageProvider {

    @Provides(type = "node", with = "models.BasicPage")
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
