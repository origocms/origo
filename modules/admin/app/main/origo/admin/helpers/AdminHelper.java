package main.origo.admin.helpers;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.ui.Element;

public class AdminHelper {

    public static String getURLForAdminAction(String dashboard, String type) {
        return routes.Dashboard.pageWithType(dashboard, type).url();
    }

    public static String getURLForAdminAction(String dashboard, String type, String identifier) {
        return routes.Dashboard.pageWithTypeAndIdentifier(dashboard, type, identifier).url();
    }

    public static Element createBasicDashboard(int weight) {
        return new Admin.Dashboard().setWeight(weight).addAttribute("class", "dashboard");
    }

    public static Element createBasicDashboard() {
        return createBasicDashboard(10);
    }

    public static Element createBasicDashboardItem() {
        return new Admin.DashboardItem().addAttribute("class", "item");
    }
}
