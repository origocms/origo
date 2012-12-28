package main.origo.admin.interceptors.settings;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminHelper;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.RootNode;

@Interceptor
public class EventSelectorDashboard {

    private static final String BASE_TYPE = "origo.admin.settings.event";
    private static final String EDIT_TYPE = BASE_TYPE + ".edit";

    /**
     * Dashboard element for the settings dashboard page.
     *
     * @return a Element that contains a dashboard element.
     */
    @Provides(type = Admin.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.SETTINGS_PAGE_TYPE)
    public static Element createDashboardItem(Provides.Context context) {

        String url = AdminHelper.getURLForAdminAction(Admin.CONTENT_PAGE_TYPE, EDIT_TYPE);

        return new Admin.DashboardItem().addAttribute("class", "item").
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Heading4().setWeight(10).setBody("Event Handlers").addAttribute("class", "title")).
                        addChild(new Element.Paragraph().setWeight(20).setBody("Select which event handler should be used for ").addAttribute("class", "description")).
                        addChild(new Element.Anchor().setWeight(30).setBody("List All").addAttribute("href", url).addAttribute("class", "link"))
                );
    }

    /**
     * Provides a type with the static name 'origo.admin.settings.event.edit'.
     *
     * @param context containing a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Types.NODE, with = EDIT_TYPE)
    public static Node createEditPage(Provides.Context context) {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Event Handlers");
        page.rootNode = (RootNode) context.node;
        return page;
    }

    @OnLoad(type = Types.NODE, with = EDIT_TYPE)
    public static void loadEditPage(OnLoad.Context context) {
        context.node.addElement(FormHelper.createFormElement(context.node, BASE_TYPE));
    }

}
