package main.origo.admin.interceptors.content;

import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.BasicPage;
import models.origo.core.Content;
import models.origo.core.RootNode;

@Interceptor
public class BasicPageAdminCreateProvider {

    @Provides(type = Core.Type.NODE, with = BasicPageAdminProvider.NEW_TYPE)
    public static Node createNewPage(Provides.Context context) {
        AdminPage page = new AdminPage(new RootNode(0));

        // TODO: Look up themevariant (and also meta) from DB instead of resetting here.
        page.rootNode.themeVariant = null;
        page.setTitle("New Basic Page");
        page.addElement(DashboardHelper.createBreadcrumb(BasicPageAdminProvider.BASE_TYPE), AdminTheme.topMeta());
        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = BasicPageAdminProvider.NEW_TYPE)
    public static void loadNewPage(OnLoad.Context context) {
        BasicPage page = new BasicPage();
        page.rootNode = new RootNode(0);
        context.attributes().put("page", page);
        context.attributes().put("lead", new Content());
        context.attributes().put("body", new Content());
        context.node().addElement(FormHelper.createFormElement(context.node(), BasicPageAdminProvider.BASE_TYPE));
    }


    /**
     * Provides a type with the static name 'origo.admin.basicpage.edit'.
     *
     * @param context containing a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Core.Type.NODE, with = BasicPageAdminProvider.EDIT_TYPE)
    public static Node createEditPage(Provides.Context context) {
        AdminPage page;

        if (context.node().getVersion() == null || context.node().getVersion() == 0) {
            page = new AdminPage(RootNode.findLatestVersionWithNodeId(context.node().getNodeId()).copy());
        } else {
            page = new AdminPage((RootNode) context.node());
        }

        // TODO: Look up themevariant (and also meta) from DB instead of resetting here.
        page.rootNode.themeVariant = null;
        page.setTitle("Edit Basic Page");
        page.addElement(DashboardHelper.createBreadcrumb(BasicPageAdminProvider.BASE_TYPE), AdminTheme.topMeta());
        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = BasicPageAdminProvider.EDIT_TYPE)
    public static void loadEditPage(OnLoad.Context context) {
        BasicPage basicPage = BasicPage.findLatestVersion(context.node().getNodeId());
        if (basicPage == null) {
            context.node().addElement(new Element.Paragraph().setWeight(10).setBody("Page '" + context.node().getNodeId() + "' does not exist."));
            return;
        }
        basicPage.rootNode = RootNode.findWithNodeIdAndSpecificVersion(context.node().getNodeId(), context.node().getVersion());

        Content leadContent = Content.findWithIdentifier(basicPage.leadReferenceId);
        Content bodyContent = Content.findWithIdentifier(basicPage.bodyReferenceId);

        context.attributes().put("page", basicPage);
        context.attributes().put("lead", leadContent);
        context.attributes().put("body", bodyContent);

        context.node().addElement(FormHelper.createFormElement(context.node(), BasicPageAdminProvider.BASE_TYPE));
    }

}