package main.origo.admin.interceptors;

import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.NavigationElement;
import models.origo.admin.AdminNavigation;

@Interceptor
public class DefaultAdminNavigationProvider {

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = "origo.admin.navigation")
    public static NavigationElement createAliasNavigation(Provides.Context.NavigationContext context) {

        AdminNavigation navigation = (AdminNavigation) context.navigation();

        // TODO: Set this selected somehow
        //boolean selected = context.node.getNodeId().equals(alias.pageId);
        return new NavigationElement().setSection("admin").
                setTitle(navigation.getText()).
                setLink(navigation.getLink()).
                setWeight(navigation.getWeight()).
                setSelected(false);
    }

}
