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
public class DefaultSettingsDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    @Provides(type = Admin.DASHBOARD_ITEM, with = Admin.SETTINGS_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addSettingsDashboardItemToFrontPage(Provides.Context context) {
        return AdminHelper.createBasicDashboardItem().
                addChild(new Element.Panel().setWeight(100).
                        addChild(new Element.Heading4().setWeight(10).setBody("Settings").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", DashboardHelper.getDashBoardURL(Admin.SETTINGS_PAGE_TYPE))));
    }

    /*
     * Creating the Node for the Settings Dashboard.
     */
    @Provides(type = Types.NODE, with = Admin.SETTINGS_PAGE_TYPE)
    public static Node addSettingsDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Settings - Dashboard");
        page.rootNode = (RootNode) context.node;

        page.addUIElement(DashboardHelper.createDashboard(Admin.SETTINGS_PAGE_TYPE, page));

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    @Provides(type = Admin.DASHBOARD, with = Admin.SETTINGS_PAGE_TYPE)
    @Relationship(parent = Admin.FRONT_PAGE_TYPE)
    public static Element addSettingsDashboardContent(Provides.Context context) {
        return AdminHelper.createBasicDashboard();
    }

    /*
     * Creating the Dashboard Items for the Settings dashboard.
     */
    @OnLoad(type = Admin.DASHBOARD, with = Admin.SETTINGS_PAGE_TYPE)
    public static void addFrontPageDashboardItems(OnLoad.Context context) {
        List<Element> elements = DashboardHelper.createDashboardItems(Admin.SETTINGS_PAGE_TYPE, context.node);
        for(Element element : elements) {
            context.node.addUIElement(element);
        }
    }

}
