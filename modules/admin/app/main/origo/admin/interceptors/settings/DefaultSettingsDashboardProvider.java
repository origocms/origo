package main.origo.admin.interceptors.settings;

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
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import play.Logger;
import views.html.origo.admin.dashboard_item;

import java.util.Map;

@Interceptor
public class DefaultSettingsDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = Admin.With.SETTINGS_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addSettingsDashboardItemToFrontPage(Node node, String withType, Map<String, Object> args) {
        return DashboardHelper.createBasicDashboardItem().
                addChild(new Element.Raw().setBody(dashboard_item.render("Settings", "", getDashboardUrl(), "View")));
    }

    @Admin.Navigation(alias="/settings", key="breadcrumb.origo.admin.dashboard.settings")
    public static String getDashboardUrl() {
        return routes.Dashboard.dashboard(Admin.With.SETTINGS_PAGE).url();
    }

    /*
     * Creating the Node for the Settings Dashboard.
     */
    @Provides(type = Admin.Type.ADMIN_NODE, with = Admin.With.SETTINGS_PAGE)
    public static Node addSettingsDashboard(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException {
        AdminPage page = AdminPage.create(node, Admin.With.SETTINGS_PAGE);
        page.setTitle("Settings - Dashboard");
        return page;
    }

    @OnLoad(type = Admin.Type.ADMIN_NODE, with = Admin.With.SETTINGS_PAGE)
    public static void loadPage(Node node) throws NodeLoadException {

        node.addElement(DashboardHelper.createBreadcrumb(Admin.With.SETTINGS_PAGE), AdminTheme.topMeta());
        try {
            node.addElement(DashboardHelper.createDashboard(node, Admin.With.SETTINGS_PAGE));
        } catch (ModuleException e) {
            // TODO: recover somehow?
            Logger.error("Unable to load dashboard", e);
        }
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.Type.DASHBOARD, with = Admin.With.SETTINGS_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addSettingsDashboardContent(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+ Admin.With.SETTINGS_PAGE).
                addChildren(DashboardHelper.createDashboardItems(node, Admin.With.SETTINGS_PAGE));
    }

}
