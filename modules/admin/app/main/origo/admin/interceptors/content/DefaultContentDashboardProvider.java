package main.origo.admin.interceptors.content;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import play.Logger;

import java.util.Map;

@Interceptor
public class DefaultContentDashboardProvider {

    /*
     * Dashboard Item for front page.
     */
    //@Provides(type = Admin.Type.DASHBOARD_ITEM, with = Core.With.CONTENT_PAGE)
    //@Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addContentDashboardItemToFrontPage(Node node, String withType, Map<String, Object> args) {
        return DashboardHelper.createBasicDashboardItem().
                setId("item.link.content").
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Heading4().setWeight(10).setBody("Content").addAttribute("class", "title")).
                        addChild(new Element.Anchor().setWeight(10).setBody("View").addAttribute("href", getDashboardUrl())));
    }

    //@Admin.Navigation(alias="/content", key="breadcrumb.origo.admin.dashboard.content")
    public static String getDashboardUrl() {
        return routes.Dashboard.dashboard(Admin.With.CONTENT_PAGE).url();
    }

    //@Admin.Navigation(alias="/content/pages", key="breadcrumb.origo.admin.dashboard.content.pages")
    public static String getDashboardPagesUrl() {
        return routes.Dashboard.dashboard(Admin.With.CONTENT_PAGE).url();
    }

    /*
     * Creating the Node for the Content page.
     */
    //@Provides(type = Core.Type.NODE, with = Core.With.CONTENT_PAGE)
    public static Node addContentDashboard(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException {
        AdminPage page = AdminPage.create(node, Admin.With.CONTENT_PAGE);
        page.setTitle("Content - Dashboard");

        node.addElement(DashboardHelper.createBreadcrumb(Admin.With.CONTENT_PAGE), AdminTheme.topMeta());
        try {
            node.addElement(DashboardHelper.createDashboard(node, Admin.With.CONTENT_PAGE));
        } catch (ModuleException e) {
            // TODO: recover somehow?
            Logger.error("Unable to load dashboard", e);
        }

        return page;
    }

    /*
     * Creating the Dashboard for the Node created above.
     */
    //@Provides(type = Admin.Type.DASHBOARD, with = Core.With.CONTENT_PAGE)
    //@Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addContentDashboardContent(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        return DashboardHelper.createBasicDashboard().
                setId("dashboard." + Admin.With.CONTENT_PAGE).
                addChildren(DashboardHelper.createDashboardItems(node, Admin.With.CONTENT_PAGE));
    }

}
