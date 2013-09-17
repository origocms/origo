package main.origo.admin.interceptors.user;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import play.Logger;

import java.util.Map;

@Interceptor
public class DefaultUserManagementDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = Admin.With.USER_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addUserDashboardItemToFrontPage(Node node, String withType, Map<String, Object> args) {
        return DashboardHelper.createBasicDashboardItem().
                setId("item.link." + Admin.With.USER_PAGE).
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.Heading4().setWeight(10).setBody("User Management").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", getDashboardUrl())));
    }

    @Admin.Navigation(alias="/user", key="breadcrumb.origo.admin.dashboard.user")
    public static String getDashboardUrl() {
        return routes.Dashboard.dashboard(Admin.With.USER_PAGE).url();
    }

    /*
     * Creating the Node for the User Dashboard.
     */
    @Provides(type = Core.Type.NODE, with = Admin.With.USER_PAGE)
    public static Node createUserDashboard(Node node, String withType, Map<String, Object> args) throws NodeLoadException {
        AdminPage page = new AdminPage(Admin.With.USER_PAGE, (RootNode) node);
        page.setTitle("User Management - Dashboard");
        page.addElement(DashboardHelper.createBreadcrumb(Admin.With.USER_PAGE), AdminTheme.topMeta());

        try {
            node.addElement(DashboardHelper.createDashboard(node, Admin.With.USER_PAGE));
        } catch (ModuleException e) {
            // TODO: recover somehow?
            Logger.error("Unable to load dashboard", e);
        }

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.Type.DASHBOARD, with = Admin.With.USER_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addUserDashboardContent(Node node, String withType, Map<String, Object> args) {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+Admin.With.USER_PAGE).
                addChildren(DashboardHelper.createDashboardItems(node, Admin.With.USER_PAGE));
    }

}
