package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Types;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;

/**
 * Dashboard is the front page of the admin UI. Any \@Provides annotation with type DASHBOARD_ITEM will be picked up and
 * displayed along with the others. This is the default implementation of a DashBoardProvider. If a module would
 * like to change this behaviour it can be done by changing the dashboard type in settings.
 */
@Interceptor
public class DefaultDashboardProvider {

    /*
     * Creating the node for the Front page.
     */
    @Provides(type = Types.NODE, with = Admin.FRONT_PAGE_TYPE)
    public static Node createPage(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Dashboard");
        page.rootNode = (RootNode) context.node;

        context.node.addElement(DashboardHelper.createDashboard(Admin.FRONT_PAGE_TYPE, context.node));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.DASHBOARD, with = Admin.FRONT_PAGE_TYPE)
    public static Element createFrontPageDashboard(Provides.Context context) {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+Admin.FRONT_PAGE_TYPE).
                addChildren(DashboardHelper.createDashboardItems(Admin.FRONT_PAGE_TYPE, context.node));
    }

}
