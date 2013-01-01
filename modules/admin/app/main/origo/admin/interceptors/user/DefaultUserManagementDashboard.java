package main.origo.admin.interceptors.user;

import main.origo.admin.annotations.Admin;
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

@Interceptor
public class DefaultUserManagementDashboard {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addUserDashboardItemToFrontPage(Provides.Context context) {
        return DashboardHelper.createBasicDashboardItem().
                setId("item.link." + Admin.USER_PAGE_TYPE).
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.Heading4().setWeight(10).setBody("User Management").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.USER_PAGE_TYPE))));
    }

    /*
     * Creating the Node for the User Dashboard.
     */
    @Provides(type = Types.NODE, with = Admin.USER_PAGE_TYPE)
    public static Node createUserDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("User Management - Dashboard");
        page.rootNode = (RootNode) context.node;

        context.node.addElement(DashboardHelper.createDashboard(Admin.USER_PAGE_TYPE, context.node));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.DASHBOARD, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addUserDashboardContent(Provides.Context context) {
        return DashboardHelper.createBasicDashboard().setId("dashboard."+Admin.USER_PAGE_TYPE).
                addChildren(DashboardHelper.createDashboardItems(Admin.USER_PAGE_TYPE, context.node));
    }

}
