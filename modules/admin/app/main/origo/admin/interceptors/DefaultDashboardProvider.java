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
     * FRONT dashboard
     */

    @Provides(type = Types.NODE, with = Admin.FRONT_PAGE_TYPE)
    public static Node createPage(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Dashboard");
        page.rootNode = (RootNode) context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.FRONT_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.FRONT_PAGE_TYPE)
    public static Element addFrontPageContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     *  Dashboard Items for FRONT page
     */

    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.CONTENT_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addContentDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.H4().setWeight(10).setBody("Content").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.CONTENT_PAGE_TYPE))));
    }

    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addUserDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.H4().setWeight(10).setBody("User Management").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.USER_PAGE_TYPE))));
    }

    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.SETTINGS_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addSettingsDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new Element.Panel().setWeight(100).
                        addChild(new Element.H4().setWeight(10).setBody("Settings").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.SETTINGS_PAGE_TYPE))));
    }

    /*
     * CONTENT dashboard
     */

    @Provides(type = Types.NODE, with = Admin.CONTENT_PAGE_TYPE)
    public static Node addContentDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Content - Dashboard");
        page.rootNode = (RootNode) context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.CONTENT_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.CONTENT_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addContentDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }


    /*
     * SETTINGS dashboard
     */

    @Provides(type = Types.NODE, with = Admin.SETTINGS_PAGE_TYPE)
    public static Node addSettingsDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Content - Dashboard");
        page.rootNode = (RootNode) context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.CONTENT_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.SETTINGS_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addSettingsDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     * USER dashboard
     */

    @Provides(type = Types.NODE, with = Admin.USER_PAGE_TYPE)
    public static Node createUserDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("User Management - Dashboard");
        page.rootNode = (RootNode) context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.USER_PAGE_TYPE, page));

        return page;
    }

    @Provides(type = Admin.DASHBOARD, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addUserDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }
}
