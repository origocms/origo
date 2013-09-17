package main.origo.admin.interceptors;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.NavigationElement;
import models.origo.admin.AdminNavigation;

import java.util.Map;

@Interceptor
public class DefaultAdminNavigationProvider {

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = "origo.admin.navigation")
    public static NavigationElement createAliasNavigation(Node node, String withType, Map<String, Object> args) {

        AdminNavigation navigation = (AdminNavigation) args.get("navigation");

        // TODO: Set this selected somehow
        //boolean selected = context.node.nodeId().equals(alias.pageId);

        NavigationElement ne = new NavigationElement();
        ne.section = "admin";
        ne.title = navigation.getText();
        ne.link = navigation.getLink();
        ne.weight = navigation.getWeight();
        ne.selected = false;
        return ne;
    }

}
