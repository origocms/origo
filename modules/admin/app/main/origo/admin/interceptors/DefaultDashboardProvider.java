package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminHelper;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
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

    /*
     * FRONT page dashboard
     */

    @Provides(type = Types.NODE, with = Admin.FRONT_PAGE_TYPE)
    public static Node createPage(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Dashboard");
        page.rootNode = (RootNode)context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.FRONT_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.FRONT_PAGE_TYPE)
    public static UIElement addFrontPageContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     *  Dashboard Items for FRONT page
     */

    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.CONTENT_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static UIElement addContentDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new UIElement(UIElement.PANEL, 10).
                        addChild(new UIElement(UIElement.HEADING4, 10, "Content").addAttribute("class", "title")).
                        addChild(new UIElement(UIElement.ANCHOR, 10, "View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.CONTENT_PAGE_TYPE))));
    }

    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static UIElement addUserDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new UIElement(UIElement.PANEL, 20).
                        addChild(new UIElement(UIElement.HEADING4, 10, "User Management").addAttribute("class", "title")).
                        addChild(new UIElement(UIElement.ANCHOR, 10, "View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.USER_PAGE_TYPE))));
    }

    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.SETTINGS_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static UIElement addSettingsDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new UIElement(UIElement.PANEL, 100).
                        addChild(new UIElement(UIElement.HEADING4, 10, "Settings").addAttribute("class", "title")).
                        addChild(new UIElement(UIElement.ANCHOR, 10, "View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.SETTINGS_PAGE_TYPE))));
    }

    /*
     * CONTENT page dashboard
     */

    @Provides(type = Types.NODE, with = Admin.CONTENT_PAGE_TYPE)
    public static Node addContentDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Content - Dashboard");
        page.rootNode = (RootNode)context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.CONTENT_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.CONTENT_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static UIElement addContentDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }


    /*
     * SETTINGS page dashboard
     */

    @Provides(type = Types.NODE, with = Admin.SETTINGS_PAGE_TYPE)
    public static Node addSettingsDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Content - Dashboard");
        page.rootNode = (RootNode)context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.CONTENT_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.SETTINGS_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static UIElement addSettingsDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     * USER page dashboard
     */

    @Provides(type = Types.NODE, with = Admin.USER_PAGE_TYPE)
    public static Node createUserDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("User Management - Dashboard");
        page.rootNode = (RootNode)context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.USER_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static UIElement addUserDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }
}
