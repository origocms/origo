package main.origo.admin.interceptors.content;

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
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", getDashboardUrl())));
    }

    @Admin.Navigation(alias="/content", key="breadcrumb.origo.admin.dashboard.content")
    public static String getDashboardUrl() {
        return routes.Dashboard.dashboard(Admin.With.CONTENT_PAGE).url();
    }

    /*
     * Creating the Node for the Content page.
     */
    @Provides(type = Core.Type.NODE, with = Admin.With.CONTENT_PAGE)
    public static Node addContentDashboard(Provides.Context context) throws NodeLoadException {
        AdminPage page = new AdminPage((RootNode) context.node());
        page.setTitle("Content - Dashboard");

        context.node().addElement(DashboardHelper.createBreadcrumb(Admin.With.CONTENT_PAGE), AdminTheme.topMeta());
        context.node().addElement(DashboardHelper.createDashboard(context.node(), Admin.With.CONTENT_PAGE));

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
                addChildren(DashboardHelper.createDashboardItems(context.node(), Admin.With.CONTENT_PAGE));
    }

}
