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
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;
import views.html.origo.admin.decorators.content;

import java.util.List;

@Interceptor
public class ContentAdminProvider {

    @Admin.Navigation(alias = "/content", key = "breadcrumb.origo.admin.dashboard.content.pages")
    public static String getDashboardUrl() {
        return routes.Application.pageWithType(Core.With.CONTENT_PAGE).url();
    }

    @Provides(type = Core.Type.NODE, with = Core.With.CONTENT_PAGE)
    public static Node addContentDashboard(Provides.Context context) throws NodeLoadException, ModuleException {
        AdminPage page = new AdminPage(Core.With.CONTENT_PAGE, (RootNode) context.node);
        page.setTitle("Content");

        context.node.addElement(DashboardHelper.createBreadcrumb(Core.With.CONTENT_PAGE), AdminTheme.topMeta());

        List<RootNode> rootNodes = RootNode.findCurrentVersions();

        List<Node> nodes = Lists.newArrayList();
        for (RootNode rootNode : rootNodes) {
            Node node = ProvidesEventGenerator.triggerInterceptor(rootNode, Core.Type.NODE, rootNode.nodeType());
            nodes.add(node);
        }

        page.addElement(new Element.Raw().setBody(content.render(nodes)));

        return page;
    }

}
