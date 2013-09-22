package main.origo.admin.interceptors.content;

import com.google.common.collect.Lists;
import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import views.html.origo.admin.dashboard_item;
import views.html.origo.admin.decorators.content;

import java.util.List;
import java.util.Map;

@Interceptor
public class ContentAdminProvider {

    /*
     * Dashboard Item for front page.
    */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = Admin.With.CONTENT_PAGE)
    @Relationship(parent = Admin.With.FRONT_PAGE)
    public static Element addContentDashboardItemToFrontPage(Node node, String withType, Map<String, Object> args) {
        return DashboardHelper.createBasicDashboardItem().
                addChild(new Element.Raw().setBody(dashboard_item.render("Content", "", getDashboardUrl(), "View")));
    }

    @Admin.Navigation(alias = "/content", key = "breadcrumb.origo.admin.dashboard.content.content")
    public static String getDashboardUrl() {
        return routes.Dashboard.dashboard(Admin.With.CONTENT_PAGE).url();
    }

    /*
     * Creating the Content listing page.
     */
    @Provides(type = Admin.Type.ADMIN_NODE, with = Admin.With.CONTENT_PAGE)
    public static Node createContentList(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        AdminPage page = AdminPage.create(node, Admin.With.CONTENT_PAGE);
        page.setTitle("Content");

        page.addElement(DashboardHelper.createBreadcrumb(Admin.With.CONTENT_PAGE), AdminTheme.topMeta());

        List<RootNode> rootNodes = RootNode.findCurrentVersions();

        List<Node> nodes = Lists.newArrayList();
        for (RootNode rootNode : rootNodes) {
            nodes.add(ProvidesEventGenerator.<Node>triggerInterceptor(rootNode, Core.Type.NODE, rootNode.nodeType()));
        }

        page.addElement(new Element.Raw().setBody(content.render(nodes)));

        return page;
    }

}
