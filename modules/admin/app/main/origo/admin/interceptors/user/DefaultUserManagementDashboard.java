package main.origo.admin.interceptors.user;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminHelper;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.*;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;

import java.util.List;

@Interceptor
public class DefaultUserManagementDashboard {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addUserDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
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
        return page;
    }

    @OnLoad(type = Types.NODE, with = Admin.USER_PAGE_TYPE)
    public static void addUserPageContent(OnLoad.Context context) throws NodeLoadException {
        context.node.addElement(DashboardHelper.createDashboard(Admin.USER_PAGE_TYPE, context.node));
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.DASHBOARD, with = Admin.USER_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addUserDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     * Creating the Dashboard Items for the User dashboard.
    */
    @OnLoad(type = Admin.DASHBOARD, with = Admin.USER_PAGE_TYPE)
    public static void addFrontPageDashboardItems(OnLoad.Context context) {
        List<Element> elements = DashboardHelper.createDashboardItems(Admin.USER_PAGE_TYPE, context.node);
        for(Element element : elements) {
            context.node.addElement(element);
        }
    }

}
