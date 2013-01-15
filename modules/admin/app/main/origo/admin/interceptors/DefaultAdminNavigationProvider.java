package main.origo.admin.interceptors;

import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.NavigationElement;

@Interceptor
public class DefaultAdminNavigationProvider {

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = "origo.admin.navigation")
    public static NavigationElement createAliasNavigation(Provides.Context context) {

        String link = (String) context.args.get("link");
        String key = (String) context.args.get("key");
        String text = (String) context.args.get("text");
        Integer weight = (Integer) context.args.get("weight");

        // TODO: Set this selected somehow
        //boolean selected = context.node.getNodeId().equals(alias.pageId);
        return new NavigationElement("admin", text, link, weight, false);
    }

}
