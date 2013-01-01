package main.origo.admin.helpers;

import controllers.origo.admin.routes;

public class AdminHelper {

    public static String getURLForAdminAction(String dashboard, String type) {
        return routes.Dashboard.pageWithType(dashboard, type).url();
    }

    public static String getURLForAdminAction(String dashboard, String type, String identifier) {
        return routes.Dashboard.pageWithTypeAndIdentifier(dashboard, type, identifier).url();
    }

}
