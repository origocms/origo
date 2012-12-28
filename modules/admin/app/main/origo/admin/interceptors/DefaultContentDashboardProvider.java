package main.origo.admin.interceptors;

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
public class DefaultContentDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.CONTENT_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addContentDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Heading4().setWeight(10).setBody("Content").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.CONTENT_PAGE_TYPE))));
    }

    /*
     * Creating the Node for the Content page.
     */
    @Provides(type = Types.NODE, with = Admin.CONTENT_PAGE_TYPE)
    public static Node addContentDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Content - Dashboard");
        page.rootNode = (RootNode) context.node;

        page.addElement(DashboardHelper.createDashboard(Admin.CONTENT_PAGE_TYPE, page));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.DASHBOARD, with = Admin.CONTENT_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addContentDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     * Creating the Dashboard Items for the Content dashboard.
     */
    @OnLoad(type = Admin.DASHBOARD, with = Admin.CONTENT_PAGE_TYPE)
    public static void addFrontPageDashboardItems(OnLoad.Context context) {
        List<Element> elements = DashboardHelper.createDashboardItems(Admin.CONTENT_PAGE_TYPE, context.node);
        for(Element element : elements) {
            context.node.addElement(element);
        }
    }
}
