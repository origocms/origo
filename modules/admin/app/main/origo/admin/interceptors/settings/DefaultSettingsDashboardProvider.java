package main.origo.admin.interceptors.settings;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import views.html.origo.admin.dashboard_item;

@Interceptor
public class DefaultSettingsDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = Admin.With.SETTINGS_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addSettingsDashboardItemToFrontPage(Provides.Context context) {
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
    @Provides(type = Core.Type.NODE, with = Admin.With.SETTINGS_PAGE)
    public static Node addSettingsDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage((RootNode) context.node());
        page.setTitle("Settings - Dashboard");

        context.node().addElement(DashboardHelper.createBreadcrumb(Admin.With.SETTINGS_PAGE), AdminTheme.topMeta());
        context.node().addElement(DashboardHelper.createDashboard(context.node(), Admin.With.SETTINGS_PAGE));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.Type.DASHBOARD, with = Admin.With.SETTINGS_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addSettingsDashboardContent(Provides.Context context) {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+ Admin.With.SETTINGS_PAGE).
                addChildren(DashboardHelper.createDashboardItems(context.node(), Admin.With.SETTINGS_PAGE));
    }

}
