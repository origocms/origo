package main.origo.admin.helpers;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.ui.UIElement;

public class AdminHelper {

    public static String getURLForAdminAction(String dashboard, String type) {
        return routes.Dashboard.pageWithType(dashboard, type).url();
    }

    public static String getURLForAdminAction(String dashboard, String type, String identifier) {
        return routes.Dashboard.pageWithTypeAndIdentifier(dashboard, type, identifier).url();
    }

    public static UIElement createBasicDashboard(int weight) {
        return new UIElement(Admin.DASHBOARD, weight).addAttribute("class", "dashboard");
    }

    public static UIElement createBasicDashboard() {
        return createBasicDashboard(10);
    }

    public static UIElement createBasicDashboardItem() {
        return new UIElement(Admin.DASHBOARD_ITEM).addAttribute("class", "item");
    }
}
