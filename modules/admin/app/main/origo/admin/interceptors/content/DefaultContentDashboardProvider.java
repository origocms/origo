package main.origo.admin.interceptors.content;

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
public class DefaultContentDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = Admin.With.CONTENT_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addContentDashboardItemToFrontPage(Provides.Context context) {
        return DashboardHelper.createBasicDashboardItem().
                setId("item.link." + Admin.With.CONTENT_PAGE).
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Heading4().setWeight(10).setBody("Content").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.With.CONTENT_PAGE))));
    }

    /*
     * Creating the Node for the Content page.
     */
    @Provides(type = Core.Type.NODE, with = Admin.With.CONTENT_PAGE)
    public static Node addContentDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Content - Dashboard");
        page.rootNode = (RootNode) context.node;

        context.node.addElement(DashboardHelper.createDashboard(Admin.With.CONTENT_PAGE, context.node));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.Type.DASHBOARD, with = Admin.With.CONTENT_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addContentDashboardContent(Provides.Context context) {
        return DashboardHelper.createBasicDashboard().
                setId("dashboard." + Admin.With.CONTENT_PAGE).
                addChildren(DashboardHelper.createDashboardItems(Admin.With.CONTENT_PAGE, context.node));
    }

}
