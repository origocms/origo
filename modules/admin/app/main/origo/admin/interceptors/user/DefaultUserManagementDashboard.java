package main.origo.admin.interceptors.user;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;

@Interceptor
public class DefaultUserManagementDashboard {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = Admin.With.USER_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addUserDashboardItemToFrontPage(Provides.Context context) {
        return DashboardHelper.createBasicDashboardItem().
                setId("item.link." + Admin.With.USER_PAGE).
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.Heading4().setWeight(10).setBody("User Management").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", routes.Dashboard.dashboard(Admin.With.USER_PAGE).url())));
    }

    /*
     * Creating the Node for the User Dashboard.
     */
    @Provides(type = Core.Type.NODE, with = Admin.With.USER_PAGE)
    public static Node createUserDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage((RootNode) context.node);
        page.setTitle("User Management - Dashboard");
        page.addElement(DashboardHelper.createBreadcrumb(Admin.With.USER_PAGE));

        context.node.addElement(DashboardHelper.createDashboard(Admin.With.USER_PAGE, context.node));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.Type.DASHBOARD, with = Admin.With.USER_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addUserDashboardContent(Provides.Context context) {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+Admin.With.USER_PAGE).
                addChildren(DashboardHelper.createDashboardItems(Admin.With.USER_PAGE, context.node));
    }

}
