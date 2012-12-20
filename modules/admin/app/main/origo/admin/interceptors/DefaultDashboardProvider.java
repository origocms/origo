package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Types;
import main.origo.core.ui.UIElement;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;

/**
 * Dashboard is the front page of the admin UI. Any \@Provides annotation with type DASHBOARD_ITEM will be picked up and
 * displayed along with the others. This is the default implementation of a DashBoardProvider. If a module would
 * like to change this behaviour it can be done by changing the dashboard type in settings.
 */
@Interceptor
public class DefaultDashboardProvider {

    public static final String TYPE = "origo.admin.dashboard";

    @Provides(type = Types.NODE, with = TYPE)
    public static Node createPage(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Dashboard");
        page.rootNode = (RootNode)context.node;

        page.addUIElement(DashboardHelper.createDashboard(TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = TYPE)
    public static UIElement addContent(Provides.Context context) {
        return new UIElement(Admin.DASHBOARD, 10);
    }

}
