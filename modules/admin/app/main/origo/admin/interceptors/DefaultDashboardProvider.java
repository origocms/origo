package main.origo.admin.interceptors;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import play.Logger;

import java.util.Map;

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
    @Provides(type = Admin.Type.ADMIN_NODE, with = Admin.With.FRONT_PAGE)
    public static Node createPage(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException {
        AdminPage page = AdminPage.create(node, Admin.With.FRONT_PAGE);
        page.setTitle("Dashboard");
        return page;
    }

    @OnLoad(type = Admin.Type.ADMIN_NODE, with = Admin.With.FRONT_PAGE)
    public static void loadPage(Node node) throws NodeLoadException {
        node.addElement(DashboardHelper.createBreadcrumb(Admin.With.FRONT_PAGE), AdminTheme.topMeta());
        try {
            node.addElement(DashboardHelper.createDashboard(node, Admin.With.FRONT_PAGE));
        } catch (ModuleException e) {
            // TODO: recover somehow?
            Logger.error("Unable to load dashboard", e);
        }
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.Type.DASHBOARD, with = Admin.With.FRONT_PAGE)
    public static Element createFrontPageDashboard(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+Admin.With.FRONT_PAGE).
                addChildren(DashboardHelper.createDashboardItems(node, Admin.With.FRONT_PAGE));
    }

    @Admin.Navigation(alias="/dashboard", key = "breadcrumb.origo.admin.dashboard.frontpage", weight = 1)
    public static String getDashboardUrl() {
        return routes.Dashboard.dashboard(Admin.With.FRONT_PAGE).url();
    }

}
