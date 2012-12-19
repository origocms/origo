package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminHelper;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.RenderingContext;
import main.origo.core.ui.UIElement;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import play.api.templates.Html;
import views.html.origo.admin.decorators.dashboard_item;

import java.util.Set;

/**
 * Dashboard is the front page of the admin UI. Any \@Provides annotation with type DASHBOARD will be picked up and
 * displayed along with the others. This is the default implementation of a DashBoardProvider. If a module would
 * like to change this behaviour it can be done by changing the dashboard type in settings.
 */
@Interceptor
public class DefaultDashboardProvider {

    public static final String TYPE = "origo.admin.dashboard";

    @Provides(type = Types.NODE, with = TYPE)
    public static Node createPage(Provides.Context context) {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Dashboard");
        page.rootNode = (RootNode)context.node;
        return page;
    }

    @OnLoad(type = Types.NODE, with = DefaultDashboardProvider.TYPE)
    public static void addContent(OnLoad.Context context) {

        Set<String> dashboardItemNames = ProvidesHelper.getAllProvidesWithForType(Admin.DASHBOARD);

        for (String dashboardItemName : dashboardItemNames) {
            context.node.addUIElement(AdminHelper.createDashboardItem(dashboardItemName, context.node));
        }
    }

}
