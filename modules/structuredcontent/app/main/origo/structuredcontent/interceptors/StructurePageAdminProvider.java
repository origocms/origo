package main.origo.structuredcontent.interceptors;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;
import views.html.origo.admin.dashboard_item;

@Interceptor
public class StructurePageAdminProvider {

    private static final String BASE_TYPE = Admin.With.CONTENT_PAGE + ".structuredpage";
    private static final String LIST_TYPE = BASE_TYPE + ".list";
    private static final String EDIT_TYPE = BASE_TYPE + ".edit";

    @Admin.Navigation(alias="/content/pages/structured", key="breadcrumb.origo.admin.dashboard.content.structuredpage")
    public static String getProviderUrl() {
        return routes.Dashboard.pageWithType(Admin.With.CONTENT_PAGE, LIST_TYPE).url();
    }

    /**
     * Dashboard element for the content dashboard page.
     *
     * @return a Element that contains a dashboard element.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.With.CONTENT_PAGE)
    public static Element createDashboardItem(Provides.Context context) {
        return new Admin.DashboardItem().
                addChild(new Element.Raw().setBody(dashboard_item.render("Structured Page", "Structured pages have several slots for content, content can be re-used in several pages.", getProviderUrl(), "List All")));
    }
}